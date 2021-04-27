title: mysql的acid的实现原理以及隔离策略详解
sticky: true
date: 2021-04-27 16:18:00
author: assertor
categories: 
 - [数据库,事务隔离级别]
---
前言：
> ACID是一组数据库设计原则，强调的是在重要业务数据以及关键任务应用的可靠性。mysql中innoDB严格遵循ACID模型，因此数据不会损坏，结果也不会因为软件崩溃或者硬件损坏等异常情况而失真。
# 原子性
    一个事务要么都做，要么都不做。mysql在这方面提供了auto-commit、commit、rollback功能。
那么mysql是如何实现事务的原子性呢？(`通过undo log)`
`undo log`名为回滚日志，是实现原子性的关键，当事务回滚时能够撤销所有已经执行成功的sql语句，他需要记录你要回滚的响应日志信息，例如  
+ 1.当你delete一条数据的时候，就需要记录这条数据的信息，回滚的时候，insert这条旧数据  
+ 2.当你update一条数据的时候，就需要记录之前的旧值，回滚的时候，根据旧值执行update操作  
+ 3.当年insert一条数据的时候，就需要这条记录的主键，回滚的时候，根据主键执行delete操作  

`undo log`记录了这些回滚需要的信息，当事务执行失败或调用了rollback，导致事务需要回滚，
便可利用`undo log`中的信息将数据回滚到修改之前的样子。
    
# 一致性
>一致性是指事务执行前后，数据处于一种合法的状态，这种状态是语义上的而不是语法上的。

    一致性是acid的目的，只有实现了aid，才能做到数据的一致性。
    
# 隔离性
   > 事务隔离是数据库处理的基础之一。隔离级别是一种配置，用于在多个事务同时进行更改和执行查询时微调性能与结果的可靠性、一致性、可重复性之间的平衡
   
   mysql提供了读未提交、读已提交、可重复度、串行化四种隔离策略（使用innodb存储引擎情形）  
   
| 事务隔离级别 | 脏读 | 不可重复读 | 幻读 |
|:---|:---:|:---:|:---:|
|读未提交（read-uncommitted）	|是	|是	|是|
|不可重复读（read-committed）	|否	|是	|是|
|可重复读（repeatable-read）	|否	|否	|是|
|串行化（serializable）	|否	|否	|否|
   
   下面描述了mysql是如何支持不同的事务级别，从最常用级别到最不常用级别
   ## 可重复读
   这是InnoDB的默认隔离级别。 同一事务中的一致读取将读取由第一次读取建立的快照（`基于mysql的mvvc多版本并发控制，每一行数据的更改都会产生一个新的版本实现`）。这意味着，如果SELECT在同一事务中发出多个普通（非锁定）语句，则这些SELECT语句的结果彼此之间也是一致的。
   
   对于锁定读取 （SELECT使用`FOR UPDATE`或`LOCK IN SHARE MODE`），`UPDATE`和`DELETE`语句，锁定取决于语句是使用具有唯一搜索条件的唯一索引还是范围类型搜索条件。
   + 对于具有唯一搜索条件的唯一索引，InnoDB仅锁定找到的索引记录，而不锁定其前的间隙。
   + 对于其他搜索条件，InnoDB 使用`gap locks`(间隙锁定)或`next-key locks`(下一键锁定) 来锁定扫描的索引范围， 以阻止其他会话插入该范围所覆盖的间隙。
  
  ### 存在的问题
  mysql在可重复读的策略下存在幻读的问题。
  幻读会在`RU`/`RC`/`RR`级别下出现，SERIALIZABLE则杜绝了幻读，但RU/RC下还会存在脏读、不可重复读，故我们就以 RR 级别来研究 幻读，排除其他干扰。
  
  `注意`：RR 级别下存在幻读的可能，但也是可以使用对记录手动加 `X锁` 的方法消除幻读。  `SERIALIZABLE` 正是对所有事务都加`X锁`才杜绝了`幻读`，但很多场景下我们的业务 sql 并不会存在 幻读 的风险。`SERIALIZABLE` 的一刀切虽然事务绝对安全，但性能会有很多不必要的损失。故可以在`RR`下根据业务需求决定是否加锁，存在幻读风险我们加锁，不存在就不加锁，事务安全与性能兼备，这也是 `RR`作为 mysql 默认隔是个事务离级别的原因，所以需要正确的理解 幻读。 
  
  这里给出我对幻读的比较白话的理解：
  > 幻读，并不是说两次读取获取的结果集不同，幻读侧重的方面是某一次的 select 操作得到的结果所表征的数据状态无法支撑后续的业务操作。更为具体一些：select 某记录是否存在，不存在，准备插入此记录，但执行 insert 时发现此记录已存在，无法插入，此时就发生了幻读。
   
  这里给出 mysql 幻读的比较形象的场景:
  事务T1
  ![事务T1](/static/img/isolation/幻读T1.png  "幻读")  
  事务T2
  ![事务T2](/static/img/isolation/幻读T2.png  "幻读")  
  > step1 T1: SELECT * FROM users WHERE id = 1;
    step2 T2: INSERT INTO users VALUES (1, 'big cat');
    step3 T1: INSERT INTO users` VALUES (1, 'big cat');
    step4 T1: SELECT * FROM users WHERE id = 1;
  
    T1 ：主事务，检测表中是否有 id 为 1 的记录，没有则插入，这是我们期望的正常业务逻辑。
     
    T2 ：干扰事务，目的在于扰乱 T1 的正常的事务执行。
    
在 `RR` 隔离级别下，`step1`、`step2` 是会正常执行的，`step3` 则会报错主键冲突，对于 T1 的业务来说执行失败的，这里 T1 就是发生了幻读，因为 T1 在 `step1` 中读取的数据状态并不能支撑后续的业务操作，T1：“见鬼了，我刚才读到的结果应该可以支持我这样操作才对啊，为什么现在不可以”。T1 不敢相信的又执行了 `step4`，发现和 `step1`读取的结果是一样的（RR下的 `MVVC机制`）。此时，幻读无疑已经发生，T1 无论读取多少次，都查不到 id = 1 的记录，但它的确无法插入这条他通过读取来认定不存在的记录（此数据已被T2插入），对于 T1 来说，它幻读了。

### 幻读问题的解决
其实 RR 也是可以避免幻读的，通过对 select 操作手动加 行X锁（`SELECT ... FOR UPDATE` 这也正是 `SERIALIZABLE` 隔离级别下会隐式为你做的事情），同时还需要知道，即便当前记录不存在，比如 id=1 是不存在的，当前事务也会获得一把记录锁（因为InnoDB的行锁锁定的是索引，故记录实体存在与否没关系，存在就加 `行X锁`，不存在就加 `next-key lock`间隙X锁），其他事务则无法插入此索引的记录，故杜绝了幻读。

在 `SERIALIZABLE` 隔离级别下，step1 执行时是会隐式的添加 `行(X)锁` / `gap(X)锁`的，从而 step2 会被阻塞，step3 会正常执行，待 T1 提交后，T2 才能继续执行（主键冲突执行失败），对于 T1 来说业务是正确的，成功的阻塞扼杀了扰乱业务的T2，对于T1来说他前期读取的结果是可以支撑其后续业务的。

所以 mysql 的幻读并非什么读取两次返回结果集不同，而是事务在插入事先检测不存在的记录时，惊奇的发现这些数据已经存在了，之前的检测读获取到的数据如同鬼影一般。  
    _不可重复读侧重表达`读-读`，幻读则是说 `读-写`，用写来证实读的是鬼影。_
    
   ## 读已提交
   即使在同一事务中，每个一致的读取都将设置并读取其自己的新快照。
   对于锁定读取（`SELECT ... FOR UPDATE`或`LOCK IN SHARE MODE`），`UPDATE` 语句和`DELETE` 语句，InnoDB仅锁定索引记录，而不锁定它们之间的间隙，因此允许在锁定记录旁边自由插入新记录。间隙锁定仅用于外键约束检查和重复键检查。
   
   由于禁用了间隙锁定，因此可能会产生幻影问题，因为其他会话可以在间隙中插入新行。
   使用READ COMMITTED具有其他效果：
   + 对于`UPDATE`或 `DELETE`语句， InnoDB仅对其更新或删除的行持有锁。MySQL评估WHERE条件后，将释放不匹配行的记录锁 。这大大降低了死锁的可能性，但是仍然可以发生。
   
   + 对于UPDATE语句，如果某行已被锁定，则InnoDB 执行“半一致”读取，将最新的提交版本返回给MySQL，以便MySQL可以确定该行是否与的WHERE条件 匹配 UPDATE。如果该行匹配（必须更新），则MySQL会再次读取该行，这一次将InnoDB其锁定或等待对其进行锁定。
   
   示例：
```sql
CREATE TABLE t (a INT NOT NULL, b INT) ENGINE = InnoDB;
INSERT INTO t VALUES (1,2),(2,3),(3,2),(4,3),(5,2);
COMMIT;
```
在这种情况下，表没有索引，因此搜索和索引扫描使用隐藏的聚集索引(`row-no`)进行记录锁定，而不是使用索引列。假设一个会话UPDATE使用以下语句执行 ：
```sql
# Session A
START TRANSACTION;
UPDATE t SET b = 5 WHERE b = 3;
```
还假设第二个会话 `UPDATE`通过在第一个会话的语句之后执行以下语句来执行：
```sql
# Session B
UPDATE t SET b = 4 WHERE b = 2;
```
在`InnoDB`执行每个 UPDATE，它首先为其读取的每一行获取一个排他锁，然后确定是否对其进行修改。如果 `InnoDB`不修改该行，则释放该锁。否则， InnoDB保留该锁直到事务结束。这会影响事务处理，如下所示。
### 示例1 使用可重复读
当使用默认`REPEATABLE READ` 隔离级别时，第一个 `UPDATE`将在读取的每一行上获取一个x锁，并且`不会释放其中的任何一个`：
```sql
x-lock(1,2); retain x-lock
x-lock(2,3); update(2,3) to (2,5); retain x-lock
x-lock(3,2); retain x-lock
x-lock(4,3); update(4,3) to (4,5); retain x-lock
x-lock(5,2); retain x-lock
```
第二个`UPDATE`尝试获取任何锁的块将立即阻止（因为第一个更新已在所有行上保留了锁），并且直到第一个`UPDATE`提交或回滚时才继续进行：
```sql
x-lock(1,2); block and wait for first UPDATE to commit or roll back
```
### 示例2 使用读已提交
使用READ COMMITTED相反，则第UPDATE一个将在读取的每一行上获取一个x锁，并为未修改的行`释放x锁`：
```sql
x-lock(1,2); unlock(1,2)
x-lock(2,3); update(2,3) to (2,5); retain x-lock
x-lock(3,2); unlock(3,2)
x-lock(4,3); update(4,3) to (4,5); retain x-lock
x-lock(5,2); unlock(5,2)
```
对于第二个`UPDATE`， `InnoDB`执行 “半一致”读取，将它读取的每一行的最新提交版本返回给MySQL，以便MySQL可以确定该行是否符合以下 `WHERE`条件 `UPDATE`：
```sql
x-lock(1,2); update(1,2) to (1,4); retain x-lock
x-lock(2,3); unlock(2,3)
x-lock(3,2); update(3,2) to (3,4); retain x-lock
x-lock(4,3); unlock(4,3)
x-lock(5,2); update(5,2) to (5,4); retain x-lock
```

但是，如果WHERE条件包括索引列并且InnoDB使用了该索引，则在获取和保留记录锁时仅考虑索引列。在下面的示例中，第一个 `UPDATE`在b = 2的每一行上获取并保留一个x锁，第二个UPDATE在尝试获取同一记录上的x锁时会被`阻塞`，因为它也使用在b列上定义的索引。
```sql
CREATE TABLE t (a INT NOT NULL, b INT, c INT, INDEX (b)) ENGINE = InnoDB;
INSERT INTO t VALUES (1,2,3),(2,2,4);
COMMIT;

# Session A
START TRANSACTION;
UPDATE t SET b = 3 WHERE b = 2 AND c = 3;

# Session B
UPDATE t SET b = 4 WHERE b = 2 AND c = 4;
```
## 读未提交
SELECT语句以非锁定方式执行，但是可能会使用行的早期版本。因此，使用此隔离级别，此类读取不一致。这也称为 `脏读`。否则，此隔离级别的工作方式类似于 `READ COMMITTED`。

## 串行化
此级别类似于`REPEATABLE READ`，但是InnoDB将所有普通SELECT 语句隐式转换为`SELECT ... LOCK IN SHARE MODE`当`autocommit`禁用。如果 `autocommit`启用，则 SELECT是其自身的事务。因此，它被认为是只读的，如果作为一致（非阻塞）读取执行并且不需要阻塞其他事务，则可以序列化。（如果其他事务已修改所选行，则要使用SELECT强制阻塞，请禁用 `autocommit`。）
    
# 持久性
> `持久性`是指事务一旦提交，它对数据库的改变就应该是永久性的。接下来的其他操作或故障不应该对其有任何影响。
    
    mysql是如何实现事务的持久性？(`redo log`)
    mysql是先把磁盘上的数据加载到内存中，在内存中对数据进行修改，再刷会磁盘中。如果此时突然宕机，内存中
    的数据就会丢失。
    提出方案：在事务提交时直接把数据写进磁盘 -> 方案存在的问题：只修改一个页面的一个字节，就要将整个页刷入内存，浪费资源效率。毕竟一个页面16kb，只改一点点东西就将16kb的内容刷入磁盘，不太合理。同时一个事务大多涉及到多个数据页，这些数据页不可能都是相邻的，属于随机IO，显然随机IO的速度是很慢的
    
于是mysql就提出了`redo log`来解决上面的问题。在进行数据修改的时候，不仅在内存中操作，还会在`redo` 的内容中记录这次操作。当事务提交时，会先将`redo log`进行刷盘（`redo log`一部分在内存中，一部分在磁盘上）。当数据库宕机重启的时候，会将`redo log`的内容恢复到数据库中，再根据 `undo log`和`bin log`来决定是回滚数据还是提交数据。

## 采用redo log的好处是什么？
  其实好处就是将`redo log`进行刷盘比数据页刷盘效率高，具体表现如下
  + `redo log`体积小，毕竟只记录了那一页修改了什么，因此体积小、刷盘快
  + `redo log`是一直往末尾追加，属于顺序IO。效率显然比随机IO来得快 
  

> 参考文章：  
1.14.2 InnoDB and the ACID Model [mysql5.7官方手册 ACID](https://dev.mysql.com/doc/refman/5.7/en/mysql-acid.html)  
2.14.7.2.1 Transaction Isolation Levels [mysql5.7官方手册 事务隔离级别](https://dev.mysql.com/doc/refman/5.7/en/mysql-acid.html)  
3.[Mysql中事务ACID实现原理(一个拿着底层薪资操着架构师的心的码农)](https://www.cnblogs.com/rjzheng/p/10841031.html)  
4.[mysql 幻读的详解、实例及解决办法(big_cat)](https://segmentfault.com/a/1190000016566788?utm_source=tag-newest)

  

        