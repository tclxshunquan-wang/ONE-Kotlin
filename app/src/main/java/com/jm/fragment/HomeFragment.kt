package com.jm.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.jm.MainActivity
import com.jm.R
import com.jm.activity.InfoActivity
import com.jm.data.utils.Toast_Short
import com.jm.data.utils.getScreenHeight
import com.jm.data.utils.getScreenWidth
import com.jm.utils.RecycleViewClickListner
import org.jetbrains.anko.*

class HomeFragment : BaseFragment() {
    var rootView: View? = null
    var home_swip: SwipeRefreshLayout? = null
    var home_recy: RecyclerView? = null
    var layout_manager: GridLayoutManager? = null
    var myAdpater: MyRecycleAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater?.inflate(R.layout.jm_home_fragment, container, false)
        return rootView!!
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        layout_manager = GridLayoutManager(activity, 2)
        rootView?.find<TextView>(R.id.head_title).apply {
            let {
                it?.text = "首页"
            }
        }
        home_swip = rootView?.findViewById(R.id.home_swip) as SwipeRefreshLayout
        home_recy = rootView?.findViewById(R.id.home_recy) as RecyclerView
        myAdpater = MyRecycleAdapter(activity)
        home_recy?.apply {
            layoutManager = layout_manager
            adapter = myAdpater
        }
        //下拉刷新
        home_swip?.apply {
            setColorSchemeColors(Color.parseColor("#2196f3"), Color.parseColor("#4caf50"), Color.parseColor("#ff9800"), Color.parseColor("#f44336"))
            setOnRefreshListener {
                if (!home_swip!!.isRefreshing) {
                    home_swip?.isRefreshing = true
                }
            }
        }
        //click
        myAdpater?.clickListener = object : RecycleViewClickListner {
            override fun onShortClick(position: Int, T: Any) {
                Toast_Short("查看详情", activity)
                    startActivity(Intent(activity, InfoActivity::class.java))
            }

            override fun onLikeClick(position: Int, T: Any) {
                Toast_Short("喜欢", activity)
            }

            override fun onHelloClick(position: Int, T: Any) {
                Toast_Short("打招呼", activity)
            }

        }
    }

    /**
     * RecycleViewAdapter
     * */

    class MyRecycleAdapter(context: Context) : RecyclerView.Adapter<MyRecycleAdapter.MyHolder>() {
        var mCt = context
        var clickListener: RecycleViewClickListner? = null
        var leftDataList = arrayListOf(
                MainActivity.leftData("个人信息", R.mipmap.null_data),
                MainActivity.leftData("会员充值", R.mipmap.null_data),
                MainActivity.leftData("我的邮箱", R.mipmap.null_data),
                MainActivity.leftData("立即更新", R.mipmap.null_data)
        )

        init {
            for (i in 0..30) {
                leftDataList.add(MainActivity.leftData("立即更新", R.mipmap.null_data))
            }
        }

        companion object {
            val IMAGE = 0x36f01
            val LIKE_IMAGE = 0x36f04
            val HELLO_IMAGE = 0x36f03
            val TEXT = 0x36f02
        }

        override fun onBindViewHolder(holder: MyHolder?, position: Int) {
            holder?.image?.apply {
                backgroundResource = leftDataList[position].pic!!
                onClick {
                    clickListener?.onShortClick(position, leftDataList[position])
                }
            }
            //打招呼
            holder?.hello?.onClick {
                clickListener?.onHelloClick(position, leftDataList[position])
            }
            //喜欢
            holder?.like?.onClick {
                clickListener?.onLikeClick(position, leftDataList[position])
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
            val rootView = layout().createView(AnkoContext.Companion.create(mCt))
            return MyHolder(rootView)
        }

        override fun getItemCount(): Int {
            return leftDataList.size
        }

        inner class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            var image: ImageView? = null
            var like: ImageView? = null
            var hello: ImageView? = null
            var txt: TextView? = null

            init {
                image = itemView?.find<ImageView>(IMAGE)
                like = itemView?.find<ImageView>(LIKE_IMAGE)
                hello = itemView?.find<ImageView>(HELLO_IMAGE)
            }

        }

        inner class layout : AnkoComponent<Context> {

            override fun createView(ui: AnkoContext<Context>) = with(ui) {
                val screenWidth = getScreenWidth(ui.ctx)
                val screenHeigh = getScreenHeight(ui.ctx)
                relativeLayout {
                    lparams {
                        width = screenWidth / 2
                        height = width
                        margin = 1
                    }
                    backgroundColor = Color.parseColor("#ffffff")
                    imageView {
                        id = IMAGE
                        lparams {
                            width = org.jetbrains.anko.matchParent
                            height = org.jetbrains.anko.matchParent
                        }
                    }
                    relativeLayout {
                        lparams {
                            width = org.jetbrains.anko.matchParent
                            height = org.jetbrains.anko.wrapContent
                            addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                            bottomMargin = dip(5)
                        }
                        leftPadding = dip(15)
                        rightPadding = dip(15)
                        imageView {
                            id = HELLO_IMAGE
                            visibility=View.GONE
                            lparams {
                                width = dip(30)
                                height = dip(30)
                                addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                                addRule(RelativeLayout.CENTER_VERTICAL)
                            }
                            backgroundResource = R.mipmap.hello
                        }
                        imageView {
                            id = LIKE_IMAGE
                            visibility=View.GONE
                            lparams {
                                width = dip(30)
                                height = dip(30)
                                addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                                addRule(RelativeLayout.CENTER_VERTICAL)
                            }
                            backgroundResource = R.mipmap.like
                        }
                    }
                }
            }
        }

    }
}
