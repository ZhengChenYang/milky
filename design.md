## Spring属性 ##
* class
* name
* scope
* singleton
* abstract</br>
当在bean标签中设置属性abstract="true",即指定该bean为抽象bean,不会被实例化,一般仅供被其他的bean继承.抽象bean,可以不指定class属性,而是在继承它的子Bean中设置class属性
.
* lazy-init
* autowired
* dependency-check
* depends-on</br>
Bean依赖关系。一般情况下无需设定。Spring会根据情况组织各个依赖关系的构建工作（这里示例中的depends-on属性非必须）。
只有某些特殊情况下，如JavaBean中的某些静态变量需要进行初始化（这是一种BadSmell，应该在设计上应该避免）。通过depends-on指定其依赖关系可保证在此Bean加载之前，首先对depends-on所指定的资源进行加载。
* autowired-candidate
* primary
* init-method
* destroy-method
* factory-method
* factory-bean


