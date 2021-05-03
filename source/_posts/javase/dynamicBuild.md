title: 动态编译、类加载及在SpringBoot中的应用
categories: 
 - [java,动态编译技术]
author: assertor
date: 2021-05-03 14:11:00
sticky: true
---
前言
> 最近在项目中遇到了需要在前端进行导出操作，由于模板的变化性较大，需要对模板的数据结构进行经常性的变更，如将key-list结构改变为key-string结构，因此需要在前端进行java代码录入，利用动态编译和类加载技术进行模板数据的适配。
# 动态编译
最开始想到要使用`动态编译`是因为我们项目的前端使用了`vue`，在前端中动态配置了一些事件，实现了在页面上动态改变前端代码。既然前端代码能放在前端，为什么`java代码`不可以呢？  
于是在网上找了些资料，知道了动态编译可以在运行态时动态编译java代码，并进行调用。查阅了很多博客后，归纳以后有以下方法实现java的动态编译。
## ToolProvider（java6提供的工具）
因为ToolProvider提供的`JavaCompiler`提供了`文件管理器`、`编译过程诊断器`等已经能够满足大多数需求，因此这里着重讲`ToolProvider`这种方式。
### 调用run方法
```java
public void complierAndRun(){
try {
	System.out.println(System.getProperty("user.dir"));
	 //动态编译
	JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
	int status = javac.run(null, null, null, "-d", "D:\\","D:/test/AlTest.java");
	if(status!=0){
		System.out.println("没有编译成功！");
	}
	
	//动态执行
	//Class clz = Class.forName("AlTest");//返回与带有给定字符串名的类 或接口相关联的 Class 对象。
	//自定义类加载器的加载路径
	MyClassLoader myClassLoader = new MyClassLoader("D:\\");
	//包名+类名
	Class clz = myClassLoader.loadClass("AlTest");
	Object o = clz.newInstance();
	Method method = clz.getDeclaredMethod("sayHello");//返回一个 Method 对象，该对象反映此 Class 对象所表示的类或接口的指定已声明方法
	String result= (String)method.invoke(o);//静态方法第一个参数可为null,第二个参数为实际传参
	System.out.println(result);
} catch (Exception e) {
	logger.error("test", e);
}
}       
``` 
+ 优点：代码简洁，提供的功能全面
+ 缺点：无法对编译过程的日志进行收集、动态编译所使用的文件依赖在jar包环境下无法导入

### getTask自定义任务管理器
```java
//存放编译过程中输出的信息
DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<>();
StandardJavaFileManager standardFileManager = javac.getStandardFileManager(diagnosticsCollector, null, null);
//自定义文件管理器，用来存储class文件
ClassJavaFileManager classJavaFileManager = new ClassJavaFileManager(standardFileManager);
List<String> options = new ArrayList<>();
options.add("-cp");
options.add(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(6)
        +System.getProperties().getProperty("path.separator")+IdDto.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(6)
        + System.getProperties().getProperty("path.separator") + SifFinConstants.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(6));
try {
    StringObject stringObject = new StringObject(new URI(className + ".java"), JavaFileObject.Kind.SOURCE, javaCode);
    JavaCompiler.CompilationTask task = javac.getTask(null, classJavaFileManager, diagnosticsCollector, options, null, Arrays.asList(stringObject));
    Boolean call = task.call();
    if (!call) {
        String compilerMessage = getCompilerMessage(diagnosticsCollector);
        log.error(compilerMessage);
        throw new BusinessException("编译错误！" + compilerMessage);
    }
} catch (Exception e) {
    throw new BusinessException(e.getMessage());
}
```
这里的关键：使用的是java程序中的`文件管理器`——ClassJavaFileManager，故对于`springboot项目`中对jar的多层嵌套打包方式不支持。
+ 优点：功能最齐全，提供了`文件管理器`、`诊断收集器`等，能够自定义编译过程中的大多数功能。
+ 缺点：代码的`可读性`不如直接运行run方法。对于springboot项目中的动态编译必须要重写文件管理器，使用`springboot`的classpath访问方式(`LaunchedURLClassLoader`)才能够正确的访问到内层依赖文件。

这里推荐[阿里愚公项目](https://github.com/alibaba/yugong)中所使用到的`动态编译工具类`，实测在java项目中好用！！但是在sptingboot项目还是有一定局限性，需要使用自定义springboot的文件管理器
话不多说，贴代码
```java
 static class SpringBootJarFileManager implements JavaFileManager {
        private URLClassLoader classLoader;
        private StandardJavaFileManager standardJavaFileManager;
        final Map<String, byte[]> classBytes = new HashMap<>();

        SpringBootJarFileManager(StandardJavaFileManager standardJavaFileManager, URLClassLoader systemLoader) {
            this.classLoader = new URLClassLoader(systemLoader.getURLs(), systemLoader);
            this.standardJavaFileManager = standardJavaFileManager;
        }

        @Override
        public ClassLoader getClassLoader(Location location) {
            return classLoader;
        }

        private List<JavaFileObject> find(String packageName) {
            List<JavaFileObject> result = new ArrayList<>();
            String javaPackageName = packageName.replaceAll("\\.", "/");
            try {
                Enumeration<URL> urls = classLoader.findResources(javaPackageName);
                while (urls.hasMoreElements()) {
                    URL ll = urls.nextElement();
                    String ext_form = ll.toExternalForm();
                    String jar = ext_form.substring(0, ext_form.lastIndexOf("!"));
                    String pkg = ext_form.substring(ext_form.lastIndexOf("!") + 1);

                    JarURLConnection conn = (JarURLConnection) ll.openConnection();
                    conn.connect();
                    Enumeration<JarEntry> jar_items = conn.getJarFile().entries();
                    while (jar_items.hasMoreElements()) {
                        JarEntry item = jar_items.nextElement();
                        if (item.isDirectory() || (!item.getName().endsWith(".class"))) {
                            continue;
                        }
                        if (item.getName().lastIndexOf("/") != (pkg.length() - 1)) {
                            continue;
                        }
                        String name = item.getName();
                        URI uri = URI.create(jar + "!/" + name);
                        String binaryName = name.replaceAll("/", ".");
                        binaryName = binaryName.substring(0, binaryName.indexOf(JavaFileObject.Kind.CLASS.extension));
                        result.add(new CustomJavaFileObject(binaryName, uri));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        public Iterable<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) throws IOException {
            Iterable<JavaFileObject> ret = null;
            if (location == StandardLocation.PLATFORM_CLASS_PATH) {
                ret = standardJavaFileManager.list(location, packageName, kinds, recurse);
            } else if (location == StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS)) {
                ret = find(packageName);
                if (ret == null || (!ret.iterator().hasNext())) {
                    ret = standardJavaFileManager.list(location, packageName, kinds, recurse);
                }
            } else {
                ret = Collections.emptyList();
            }
            return ret;
        }

        @Override
        public String inferBinaryName(Location location, JavaFileObject file) {
            String ret = "";
            if (file instanceof CustomJavaFileObject) {
                ret = ((CustomJavaFileObject) file).binaryName;
            } else {
                ret = standardJavaFileManager.inferBinaryName(location, file);
            }
            return ret;
        }

        @Override
        public boolean isSameFile(FileObject a, FileObject b) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean handleOption(String current, Iterator<String> remaining) {
            return standardJavaFileManager.handleOption(current, remaining);
        }

        @Override
        public boolean hasLocation(Location location) {
            return location == StandardLocation.CLASS_PATH || location == StandardLocation.PLATFORM_CLASS_PATH;
        }

        @Override
        public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
            classBytes.clear();
        }

        @Override
        public int isSupportedOption(String option) {
            return -1;
        }

        public Map<String, byte[]> getClassBytes() {
            return new HashMap<String, byte[]>(this.classBytes);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind,
                                                   FileObject sibling) throws IOException {
            if (kind == JavaFileObject.Kind.CLASS) {
                return new MemoryOutputJavaFileObject(className, classBytes);
            } else {
                return standardJavaFileManager.getJavaFileForOutput(location, className, kind, sibling);
            }
        }
    }

    static class CustomJavaFileObject implements JavaFileObject {
        private String binaryName;
        private URI uri;
        private String name;

        public String binaryName() {
            return binaryName;
        }

        public CustomJavaFileObject(String binaryName, URI uri) {
            this.uri = uri;
            this.binaryName = binaryName;
            name = uri.getPath() == null ? uri.getSchemeSpecificPart() : uri.getPath();
        }

        @Override
        public Kind getKind() {
            return Kind.CLASS;
        }

        @Override
        public boolean isNameCompatible(String simpleName, Kind kind) {
            String baseName = simpleName + kind.extension;
            return kind.equals(getKind()) && (baseName.equals(getName()) || getName().endsWith("/" + baseName));
        }

        @Override
        public NestingKind getNestingKind() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Modifier getAccessLevel() {
            throw new UnsupportedOperationException();
        }

        @Override
        public URI toUri() {
            return uri;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public InputStream openInputStream() throws IOException {
            return uri.toURL().openStream();
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Writer openWriter() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getLastModified() {
            return 0;
        }

        @Override
        public boolean delete() {
            throw new UnsupportedOperationException();
        }
    }
```

`动态类加载`，这里就不详细介绍，因为动态编译后的class字节码要被实例化，可以使用反射、类加载，因为编译后的字节码直接存在内存中，这里优先使用类加载的方式。
```java
 public Class<?> loadClass(String name, Map<String, byte[]> classBytes) throws Exception {
            ClassLoader loader = new ClassLoader() {
                @Override
                public Class<?> loadClass(String name) throws ClassNotFoundException {
                    Class<?> r = null;
                    if (classBytes.containsKey(name)) {
                        byte[] buf = classBytes.get(name);
                        r = defineClass(name, buf, 0, buf.length);
                    } else {
                        r = classLoader.loadClass(name);
                    }
                    return r;
                }
            };
            return loader.loadClass(name);
        }
```
## 使用RunTime来进行动态编译
这种方式相当于调用命令行javac命令的方式，这种非api的方式比较灵活，但是对于初学者来说不建议，出现问题的概率较高。
```java
Runtime runtime=Runtime.getRuntime();
	Process process=runtime.exec("java -cp D:/myjava  HelloWorld");
	InputStream inputStream=process.getInputStream();
	BufferedReader fBufferedInputStream=new BufferedReader(new InputStreamReader(inputStream));
	String string="";
	while((string=fBufferedInputStream.readLine())!=null)
	{
		System.out.println(string);
	}
```

# 排坑心得
## 动态编译技术的认识、运用、上项目过程
1. 在分析了项目需求，对于项目中需求有了清晰的认识后，确认了使用动态编译方案
2. 在本地调试好后，上服务器功能挂掉，通过服务器日志、增加诊断收集器功能定位到是导入不进去jar包
3. 在定位问题后，自己尝试解决（压缩jar包，在javac命令中引入依赖包），后来觉得实现方式不太对，找了部门大佬沟通后终于定位到是springboot特有的jar包打包方式
4. 尝试过使用上传class文件再进行动态类加载（这样就失去了动态编译的意义）
5. 目前是还没有做太多安全相关的工作，后面会使用非对称加密、脱敏处理等手段对动态编译的代码进行检测和保护。（因为动态编译是一个双刃剑，特别是放在web程序中，相当于对外界说我的程序是公共的，大家都来看。所以安全保护措施就十分重要）
## 心得
1. 在定位问题时，如果环境不一样，尽可能去创造一个相同环境在本地测试
2. 在遇到新技术时还是要提前去了解原理再使用
3. 还要是熟悉java生态，java se、java ee、spring、springboot、springcloud等等，在问题定位时能够在对不同的java架构下可能存在的环境问题不同

> 参考文章：  
1.[阿里愚公项目动态编译工具类](https://github.com/alibaba/yugong)  
2.[博客—springboot 2下java代码动态编译动态加载实现](https://blog.csdn.net/vertexz/article/details/107326470)  
3.[github issue—对于动态编译来springboot下的应用讨论](https://github.com/michaelliao/compiler/issues)  
4.[博客—java实现动态编译并动态加载](https://blog.csdn.net/zhoufanyang_china/article/details/82767406)

  