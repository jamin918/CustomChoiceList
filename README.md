# CustomChoiceList
ListView实现item单选、多选效果  

![](https://github.com/jamin918/gif_repository/blob/master/ListView_item_choice.gif)

ListView中有一个属性：android:choiceMode,对应三个可选值：
	
* singleChoice 单选
*  multipleChoice 多选
*  none 默认情况，没有选中效果  

在ListView的布局中设置了android:choiceMode属性后，item布局需要实现checkable，才有选中效果。

那么我们先来看一下这个checkable接口：

```
/**
 * Defines an extension for views that make them checkable.
 *
 */
public interface Checkable {
    
    /**
     * Change the checked state of the view
     * 
     * @param checked The new checked state
     */
    void setChecked(boolean checked);
        
    /**
     * @return The current checked state of the view
     */
    boolean isChecked();
    
    /**
     * Change the checked state of the view to the inverse of its current state
     *
     */
    void toggle();
}
```
接口很简单，就三个方法：

* setChecked(boolean checked) 设置是否选中。当我们点击item的时候，会调用这个方法。
* boolean isChecked()  判断是否选中。
* toggle()  开关，如果当前是选中的状态，调用该方法后取消选中，反之，选中。

####实现单选效果：

1、 ListView布局中android:choiceMode设置为singleChoice。
2、选取实现了checkable接口的View或者ViewGroup作为item布局控件。

* 当item展示的数据比较简单，例如就是一段文本，item布局可以直接使用系统自带的CheckedTextView控件，该控件有一个属性：android:checkMark="?android:listChoiceIndicatorSingle"为单选样式；“?android:listChoiceIndicatorMultiple”为多选样式。若要修改显示的样式，可以自己写一个selector,然后checkMark指定为这个selector。例如：

 在drawable文件夹下面创建一个ic_hideable_item.xml文件。
 
```
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_checked="false" android:drawable="@mipmap/ic_hideable_item_unchecked" />
    <item android:drawable="@mipmap/ic_hideable_item_checked" />
</selector>
```

checkMark指定为上面的那个xml文件：
```
<CheckedTextView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/tv_single_choice"
    android:layout_width="match_parent"
	android:layout_height="?android:attr/listPreferredItemHeightSmall"
    android:textSize="14sp"
    android:gravity="center_vertical"
    android:checkMark="@drawable/ic_hideable_item"
    android:paddingLeft="16dp"
    android:paddingRight="16dp">
</CheckedTextView>
```

####实现多选效果：
1、 ListView布局中android:choiceMode设置为multipleChoice。
2、选取实现了checkable接口的View或者ViewGroup作为item布局控件。
这里笔者自定义一个控件实现Checkable接口。代码如下：

```
public class CheckableLayout extends RelativeLayout implements Checkable {

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    private boolean mChecked;

    public CheckableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void setChecked(boolean b) {

        if (b != mChecked){
            mChecked = b;
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {

        setChecked(!mChecked);
    }


    @Override
    protected int[] onCreateDrawableState(int extraSpace) {

        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

        if (isChecked()) mergeDrawableStates(drawableState, CHECKED_STATE_SET);

        return drawableState;
    }
}
```

应用到item布局：

```
<com.jm.customchoicelist.CheckableLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal">


    <TextView
        android:id="@+id/tv_content"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:duplicateParentState="true"
        android:layout_alignParentLeft="true"
        android:layout_centerVertical="true"
        android:layout_marginLeft="30dp"
        android:textColor="@color/hideable_text_color"
        tools:text="测试数据"/>


    <ImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:duplicateParentState="true"
        android:layout_alignParentRight="true"
        android:layout_centerVertical="true"
        android:layout_marginRight="20dp"
        android:src="@drawable/ic_hideable_item"/>

</com.jm.customchoicelist.CheckableLayout>
```

注意到上面TextView、ImageView控件中的android:duplicateParentState属性，
该属性表示当前控件是否跟随父控件的状态（点击、焦点等）。若将TextView的该属性置为false，则文字无变色效果；若将ImageView的该属性置为false，则无选中效果。

最后怎样获取选中item对应的位置呢？

* 单选---> 通过ListView的getCheckedItemPosition()获取选中的位置。
* 多选---> 通过ListView的getCheckedItemPositions()得到一个SparseBooleanArray，key为position，value为是否选中。

```
mSingleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int checkedItemPosition = mSingleListView.getCheckedItemPosition();
                Toast.makeText(MainActivity.this, "you chose item " + checkedItemPosition, Toast.LENGTH_SHORT).show();
            }
        });


        mMultipleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray checkedItemPositions = mMultipleListView.getCheckedItemPositions();
                boolean isChecked = checkedItemPositions.get(position);
                Toast.makeText(MainActivity.this, "item " + position + " isChecked=" + isChecked, Toast.LENGTH_SHORT).show();
            }
        });
```
