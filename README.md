ViewDragHelper的使用

> 声明：非原创 



## ViewDragHelper是什么

viewDragHelper是写自定义ViewGroups的工具类，对于用户处理views在它的父容器里面的拖动以及相对应的响应，它提供了丰富有用的操作和状态跟踪的API



## ViewDragHelper创建

![image](http://ws4.sinaimg.cn/mw690/534fc2d6ly1fmeeetkjjsj20kn030t8x.jpg)

1. Viewgroup——与 ViewDragHelper 相关联的 ViewGroup。
2. [ViewDragHelper.Callback](https://developer.android.google.cn/reference/android/support/v4/widget/ViewDragHelper.Callback.html) cb ——ViewDragHelper.Callback 是个回调

### ViewDragHelper.Callback

ViewDragHelper 提供了一系列回调，一下是全部回调 

```java
// 控制横轴的移动距离 
int clampViewPositionHorizontal(View child, int left, int dx)
//控制纵轴的移动距离 
int clampViewPositionVertical(View child, int top, int dy)
// 获取子视图的Z值
int getOrderedChildIndex(int index)
//获取视图在横轴移动的距离 
int getViewHorizontalDragRange(View child)
//获取视图在纵轴移动的距离 
int getViewVerticalDragRange(View child)
//处理当用户触碰边界移动开始的回调 
void    onEdgeDragStarted(int edgeFlags, int pointerId)
//处理边界被锁定时的回调 
boolean onEdgeLock(int edgeFlags)
//处理边界被触碰时的回调 
void    onEdgeTouched(int edgeFlags, int pointerId)
//当视图被捕获时的回调 
void    onViewCaptured(View capturedChild, int activePointerId)
//当视图的拖动状态改变的时候的回调 
void    onViewDragStateChanged(int state)
//当捕获的视图位置发生改变的时候的回调 
void    onViewPositionChanged(View changedView, int left, int top, int dx, int dy)
//当视图的拖动被释放的时候的回调 
void    onViewReleased(View releasedChild, float xvel, float yvel)
// 判断此时的视图是否为想要捕获的视图时会调用 
abstract boolean    tryCaptureView(View child, int pointerId)
```



作为初学者，我们要用**最少的代码把ViewDragHelper创建出来**



ViewDragHelper 用哪几个回调能构成最简单能运行的实例呢？

```java
// 决定了是否需要捕获这个 child，只有捕获了才能进行下面的拖拽行为
abstract boolean    tryCaptureView(View child, int pointerId)  


// 修整 child 水平方向上的坐标，left 指 child 要移动到的坐标，dx 相对上次的偏移量
int clampViewPositionHorizontal(View child, int left, int dx)

// 修整 child 垂直方向上的坐标，top 指 child 要移动到的坐标，dy 相对上次的偏移量
int clampViewPositionVertical(View child, int top, int dy)


// 手指释放时的回调
void    onViewReleased(View releasedChild, float xvel, float yvel)
```



## ViewDragHelper使用



### 拖拽的相关方法



```java
/** 是否应该拦截 children 的触摸事件，
*只有拦截了 ViewDragHelper 才能进行后续的动作
*
*将它放在 ViewGroup 中的 onInterceptTouchEvent() 方法中就好了
**/
boolean shouldInterceptTouchEvent(MotionEvent ev)

/** 处理 ViewGroup 中传递过来的触摸事件序列
*在 ViewGroup 中的 onTouchEvent() 方法中处理
*/
void    processTouchEvent(MotionEvent ev)
```



### 拖拽后回弹

#### 相关方法

```java
//将 child 安置到坐标 (final Left,final Top) 的位置。
settleCapturedViewAt(int finalLeft, int finalTop)
```







```java
// 在此方法中记录拖拽前的坐标
void onViewCaptured(View capturedChild, int activePointerId)
```



#### 思路：

1. 在 onViewCaptured() 方法中 或者 viewgroup的onLayout() 记录拖拽前的坐标。
2. 在 onViewReleased() 方法中调用 settleCapturedViewAt() 方法来重定位 child。



### 边缘触发

**相关API**

```java
//处理当用户触碰边界移动开始的回调 
void    onEdgeDragStarted(int edgeFlags, int pointerId)
//处理边界被锁定时的回调 
boolean onEdgeLock(int edgeFlags)
//处理边界被触碰时的回调 
void    onEdgeTouched(int edgeFlags, int pointerId)
```

**思路**

1. tryCaptureView 返回true使得view可以被捕获 
2. onEdgeDragStarted 中使用  captureChildView 捕获特定的view
3. mDragHelper.setEdgeTrackingEnabled() 设置边缘出发



### 移动button

因为 Button 本身能够响应点击事件，那么 ViewDragHelper 移动button需要特殊处理 

这两个方法只要返回值大于 0 ，那么它就可以滑动。

```java
@Override
public int getViewHorizontalDragRange(View child) {
​    return 1;
}

@Override
public int getViewVerticalDragRange(View child) {
​    return 1;
}
```



## 注意

1. **tryCaptureView() 方法返回 true** 时才会导致下面的回调方法被调用 
2. 边缘触发的**边缘**指的是 这个viewgroup的边缘，而**非屏幕的边缘**



## 参考

1. http://blog.csdn.net/briblue/article/details/73730386#t7
2. http://blog.csdn.net/lmj623565791/article/details/46858663
3. http://blog.csdn.net/lepaitianshi/article/details/50961596
