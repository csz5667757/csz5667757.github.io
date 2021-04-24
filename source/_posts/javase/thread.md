title: 多线程与高并发学习笔记（一）
author: assertor
top: true
categories: 
 - [java,多线程] 
tags: 多线程
sticky: false
abbrlink: 5868
date: 2020-4-5 11:25:21
---

# java内存模型

作用：jmm是一种规范，它规范了jvm是如何与计算机内存协同工作的，它规定了一个线程如何和何时能看到其它线程修改过的共享变量的值，以及在必须时如何同步的访问共享变量

## 堆

运行时的数据区，优势是可以动态分配内存大小，垃圾回收器可以自动收走不在需要的数据。缺点是由于需要动态分配内存，存取的速度较慢

## 栈

存取速度快，仅次于计算机里的寄存器，栈的数据可以共享。缺点是存储数据的大小是确定的，缺乏灵活性。栈中存储了基本数据类型变量，如int，short, long,  float, double,char,byte, boolean





![image-20200229160121072](/static/img/image-20200229160121072.png "")



![image-20200229160331686](/static/img/image-20200229160331686.png "")

# 并发的优势与风险

![image-20200229161133437](/static/img/image-20200229161133437.png "")



# 多线程

线程安全性：当多个线程访问某个类时，不管运行时环境采用**何种调度方式**或者这些进程将如何交替执行，并且在主调代码中**不需要任何额外的同步或协同**，这个类都能表现出**正确的行为**，那么就称这个类是线程安全的。

+ 原子性：提供了互斥访问，同一时刻只能有一个线程来对它进行操作

![image-20200229184042558](/static/img/image-20200229184042558.png "")

CAS算法：比较和交换 在Atomic包里用法较多，比如AtomicInteger的incrementAndGet方法

调用了Unsafe类里的native方法getAndAddInt

```java
public final int getAndAddInt(Object var1, long var2, int var4) {
        int var5;
        do {
            var5 = this.getIntVolatile(var1, var2);
        } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
    //通过getIntVolatile方法获取var1对象的变量在主存中的最新值var5与var2相比较，如过相同才会执行相加操作，不相同会继续读取在主存中的值
        return var5;
    }
```



+ 可见性：一个线程对主内存的修改可以及时的被其他线程观察到

![image-20200229184442326](/static/img/image-20200229184442326.png "")

![image-20200229184731544](/static/img/image-20200229184731544.png "")

![image-20200229184709974](/static/img/image-20200229184709974.png "")

应用场景：

![image-20200229185347816](/static/img/image-20200229185347816.png "")

+ 有序性：一个线程观察其它线程中的指令执行顺序，由于指令重排序的存在，该观察结果一般杂乱无序

  ![image-20200229190220735](/static/img/image-20200229190220735.png "")

  ***

![image-20200229185840173](/static/img/image-20200229185840173.png "")

***

![image-20200229185915892](/static/img/image-20200229185915892.png "")

***

![image-20200229190005066](/static/img/image-20200229190005066.png "")

## 安全发布对象

![image-20200302110803490](/static/img/image-20200302110803490.png "")

