---
title: 面试经历
author: assertor
top: true
categories: 面试经历
tags: java面试
sticky: true
date: 2021-04-21 21:28:00
---

# 记录2021.3.23 面试记录
0.之前做过哪些项目，最近的一次项目主要负责什么
1.hashmap数据结构的底层实现原理
2.hashmap是否是线程安全的？有哪些线程安全的类来代替hashmap
3.hashtable、concurrenthashmap底层是如何实现线程安全的？
4.java中创建多线程有哪些方式？
5.如果要线程执行完有返回值要用那种方式创建多线程？
6.线程池有哪几种实现？
7.如果要你设计一个线程池有哪些入参？
8.项目中出现慢sql，已经定位到具体的sql语句，如何解决？
9.activiti工作流实现原理？
10.项目中是如何实现分布式事务的？
11.分布式事务的底层实现由哪些方案？
12.springcloud有哪些组件？
13.spring的AOP实现原理？
14.JDK动态代理有哪些，实现原理？

# 记2021.4.21面试记录
0.问了一下现在做的项目的规模，以及服务、中台是怎样划分的；对于加班能够接受的范围
1.之前做过数据库设计？数据库设计有哪些要点、规范（答到了满足业务需求、还有长度的设定、满足范式）
2.数据库生成id的长度是多少，以及id生成的策略（答了mp生成id的策略，使用了雪花算法以及使用物理地址、时间戳散列）
3.数据库如何满足第三范式，以及为什么要满足第三范式？（这个只回答了三范式的概念，没有具体答出三范式的好处）
4.explain查询计划中有哪些属性，以及对应的作用（答了row、key、using index（没有这个属性QAQ））
5.查询计划中key属性的作用，是否有了解过key_length（答了key用来看是否是主键索引，其实是看命中索引的类型）
6.在设计表的时候是否考虑到索引，唯一索引、普通索引的区别（平时没有用到索引，太久了都忘了）
7.项目中使用的数据库隔离级别是哪个，数据库的事务隔离级别有哪些？（读未提交、读已提交、可重复读、串行化四种，居然忘了读未提交）
8.不可重复读级别与读已提交事务隔离级别有什么区别，举例说明（居然答到了CAS的ABA问题上去）
9.服务中使用了springCloud中那个组件来做网关？（GateWay）
10.如果有一个请求是否能直接请求到自己微服务的接口（饶了半天最终说了是自己微服务会对cookie做校验）
11.一个request包含哪些部分（请求头、请求体）
12.cookie是存放在request的哪个部分（在HttpServletRequest下是有专门属性存放cookie）
13.网关是如何做登录校验的？使用了AOP？（使用过滤器）
14.在springboot中下载文件如何与前端交互（通过请求的outputStream方法返回给前端）
15.如何返回使前端知道文件的类型（通过传输文件名）
16.有什么想补充或者想问的


持续更新中....
