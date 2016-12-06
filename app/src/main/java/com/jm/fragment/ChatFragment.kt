package com.jm.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.jm.MainActivity
import com.jm.R
import com.jm.activity.IMActivity
import com.jm.data.utils.XLog
import com.jm.data.utils.getScreenWidth
import com.jm.fragment.ui.ChatFragmentUI
import com.jm.utils.ListViewDecoration
import com.jm.utils.RecycleViewClickListner
import com.jm.utils.RecycleViewShortClickListner
import com.yanzhenjie.recyclerview.swipe.*
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener
import org.jetbrains.anko.*

class ChatFragment : BaseFragment() {
    var rootView:View?=null
    var myAdapter:RecycleViewAdapter?=null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
         rootView=ChatFragmentUI().createView(AnkoContext.create(activity))
        return rootView!!
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rootView?.find<SwipeMenuRecyclerView>(R.id.chat_recycle)?.apply {
            isLongPressDragEnabled=true
            isItemViewSwipeEnabled=false
            // 监听拖拽和侧滑删除，更新UI和数据。
            setOnItemMoveListener(object: OnItemMoveListener {
                override fun onItemDismiss(position: Int) {
                }
                override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
                    myAdapter?.notifyItemMoved(fromPosition, toPosition)
                    return true
                }
            })
            // 设置菜单创建器
            setSwipeMenuCreator(swipeMenuCreator(activity))
            // 设置菜单监听
            setSwipeMenuItemClickListener { closeable, adapterPosition, menuPosition, direction ->
                closeable.smoothCloseMenu()//关闭点击菜单
                if(direction==SwipeMenuRecyclerView.RIGHT_DIRECTION){
                    if(menuPosition==0){//置顶
                        Toast.makeText(activity, "list第$adapterPosition 置顶成功", Toast.LENGTH_SHORT).show();
                        myAdapter?.notifyItemMoved(adapterPosition, 0)
                    }else if(menuPosition==1){//删除
                        Toast.makeText(activity, "list第$adapterPosition  删除成功", Toast.LENGTH_SHORT).show();
                        myAdapter?.notifyItemRemoved(menuPosition)
                    }

                }
            }
            //设置分割线
            addItemDecoration(ListViewDecoration(activity))
            //adapter
            layoutManager = LinearLayoutManager(activity)
            myAdapter=RecycleViewAdapter(activity)
            adapter=myAdapter
            myAdapter?.recycleViewClick=object :RecycleViewShortClickListner{
                override fun onShortClick(position: Int, T: Any) {
                    startActivity(Intent(activity,IMActivity::class.java))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        myAdapter?.notifyDataSetChanged()
    }

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    inner class swipeMenuCreator(ctx: Context): SwipeMenuCreator {
        var mCt: Context =ctx
        val screenWidth = getScreenWidth(mCt)
        override fun onCreateMenu(swipeLeftMenu: SwipeMenu?, swipeRightMenu: SwipeMenu?, viewType: Int) {
            swipeRightMenu?.apply {
                addMenuItem(SwipeMenuItem(mCt).setText("置顶").setTextColor(mCt.resources.getColor(R.color.white)).setBackgroundColor(mCt.resources.getColor(R.color.grey_a400)).setWidth(screenWidth/4).setHeight(ViewGroup.LayoutParams.MATCH_PARENT))
                addMenuItem(SwipeMenuItem(mCt).setText("删除").setTextColor(mCt.resources.getColor(R.color.white)).setBackgroundColor(mCt.resources.getColor(R.color.main)).setWidth(screenWidth/4).setHeight(ViewGroup.LayoutParams.MATCH_PARENT))
            }
        }
    }
    /**x
     * recycleView Adapter
     */

    inner class RecycleViewAdapter(ctx: Context): SwipeMenuAdapter<RecycleViewAdapter.Viewholder>() {
        var mCt: Context =ctx
        var recycleViewClick: RecycleViewShortClickListner?=null
        var DataList = arrayListOf(
                MainActivity.leftData("个人信息",R.mipmap.ic_launcher,"http://pic250.quanjing.com/imageclk005/ic01430270.jpg"),
                MainActivity.leftData("会员充值", R.mipmap.ic_launcher,"http://pic250.quanjing.com/imageclk007/ic01704475.jpg"),
                MainActivity.leftData("我的邮箱", R.mipmap.ic_launcher,"http://pic250.quanjing.com/imageclk009/ic02900107.jpg")
        )
        init{
            for (i in 0..20){
                DataList.add(MainActivity.leftData("会员充值", R.mipmap.ic_launcher,"http://pic250.quanjing.com/imageclk007/ic01704475.jpg"))
            }
        }
        override fun onBindViewHolder(holder: Viewholder, position: Int) {
            holder.name?.text=DataList[position].name
            holder.rootView?.setOnClickListener {
                recycleViewClick?.onShortClick(position,DataList[position])
            }
        }

        override fun getItemCount(): Int {
            return DataList.size
        }

        override fun onCreateContentView(parent: ViewGroup?, viewType: Int): View {
            return ItemView().createView(AnkoContext.create(mCt))
        }

        override fun onCompatCreateViewHolder(realContentView: View?, viewType: Int): Viewholder{
            return Viewholder(realContentView!!)
        }
        inner class Viewholder(itemView:View): RecyclerView.ViewHolder(itemView){
            var name: TextView?=null
            var rootView:View?=null
            init {
                name=itemView.find<TextView>(R.id.chat_item_name)
                rootView=itemView.find<RelativeLayout>(R.id.chat_item_rel)
            }
        }
    }


    inner class ItemView: AnkoComponent<Context> {
        override fun createView(ui: AnkoContext<Context>) = with(ui) {
            relativeLayout {
                lparams {
                    width = matchParent
                    height = matchParent
                }
                include<LinearLayout>(R.layout.jm_chat_item).apply {
                    lparams {
                        width = matchParent
                        height = matchParent
                    }
                    find<RelativeLayout>(R.id.chat_item_rel).apply {
                        setOnTouchListener { view, motionEvent ->
                            XLog.v("---------","${motionEvent.action}-->>>")
                            when(motionEvent.action){
                                MotionEvent.ACTION_MOVE ->{
                                    backgroundColor=resources.getColor(R.color.grey_a400,null)
                                }
                                MotionEvent.ACTION_UP->{
                                    backgroundColor=resources.getColor(R.color.white,null)
                                }
                                MotionEvent.ACTION_CANCEL->{
                                    backgroundColor=resources.getColor(R.color.white,null)
                                }
                            }
                            false
                        }
                    }
                }
            }
        }
    }
}
