package a.dev.mobile.gthread


import a.dev.mobile.gthread.HelpMy.arrayIntegerFromCursor
import a.dev.mobile.gthread.HelpMy.arrayStringFromCursor
import a.dev.mobile.gthread.databinding.RecyclerItemModelGBinding
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FrgG : Fragment() {


    lateinit var mListener: OnGSelected

    lateinit var id: IntArray
    lateinit var desc1: Array<String>
    lateinit var desc2: Array<String>

    lateinit var threadPitch: Array<String>
    lateinit var threadPer: Array<String>

    lateinit var class_: Array<String>

    lateinit var exMajorDiaMax: Array<String>
    lateinit var exMajorDiaMin: Array<String>
    lateinit var exPitchDiaMax: Array<String>

    lateinit var exPitchDiaMin: Array<String>
    lateinit var exMinorDiaMax: Array<String>

    lateinit var inMinorDiaMin: Array<String>
    lateinit var inMinorDiaMax: Array<String>

    lateinit var inPitchDiaMax: Array<String>
    lateinit var inPitchDiaMin: Array<String>

    lateinit var inMajorDiaMin: Array<String>

    lateinit var inTapDrill: Array<String>

    private lateinit var db: SQLiteDatabase
    private lateinit var cursor: Cursor
    lateinit var sp: SharedPreferences

    companion object {
        fun newInstance(): FrgG {
            return FrgG()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sp = PreferenceManager.getDefaultSharedPreferences(context)
        if (context is OnGSelected) {
            mListener = context
        } else {
            throw ClassCastException("$context must implement OnGSelected.")
        }

        db = DbSQLiteHelper(context as MainActivity).readableDatabase

        // cursor = db.rawQuery("select DISTINCT designation_2, thread_per, designation_1, thread_pitch from G  ", null)
        cursor = db.rawQuery("select * from G  ", null)




        id = arrayIntegerFromCursor(cursor, DBColumn.ID)

        desc1 = arrayStringFromCursor(cursor, DBColumn.DESC1)
        threadPitch = arrayStringFromCursor(cursor, DBColumn.THREAD_PITCH)

        desc2 = arrayStringFromCursor(cursor, DBColumn.DESC2)
        threadPer = arrayStringFromCursor(cursor, DBColumn.THREAD_PER)

        class_ = arrayStringFromCursor(cursor, DBColumn.CLASS_)

        exMajorDiaMax = arrayStringFromCursor(cursor, DBColumn.EX_MAJOR_DIA_MAX)
        exMajorDiaMin = arrayStringFromCursor(cursor, DBColumn.EX_MAJOR_DIA_MIN)

        exPitchDiaMax = arrayStringFromCursor(cursor, DBColumn.EX_PITCH_DIAM_MAX)
        exPitchDiaMin = arrayStringFromCursor(cursor, DBColumn.EX_PITCH_DIAM_MIN)

        exMinorDiaMax = arrayStringFromCursor(cursor, DBColumn.EX_MINOR_DIA_MAX)

        inMajorDiaMin = arrayStringFromCursor(cursor, DBColumn.IN_MAJOR_DIA_MIN)

        inMinorDiaMax = arrayStringFromCursor(cursor, DBColumn.IN_MINOR_DIA_MAX)
        inMinorDiaMin = arrayStringFromCursor(cursor, DBColumn.IN_MINOR_DIA_MIN)

        inPitchDiaMax = arrayStringFromCursor(cursor, DBColumn.IN_PITCH_DIAM_MAX)
        inPitchDiaMin = arrayStringFromCursor(cursor, DBColumn.IN_PITCH_DIAM_MIN)

        inTapDrill = arrayStringFromCursor(cursor, DBColumn.IN_TAP_DRILL)





        cursor.close()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.recycler_list_content, container, false)
        val activity = activity as Context
        val recycleView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recycleView.layoutManager = GridLayoutManager(activity, 1)

        //если хотим добавить разделители между ячейками
        //recycleView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        recycleView.adapter = GListAdapter(activity)







        return view
    }

    //==============================
    internal inner class GListAdapter(context: Context) : RecyclerView.Adapter<ViewHolder>() {

        private val layoutInflater = LayoutInflater.from(context)

        override fun getItemCount(): Int {
            return desc1.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val recyclerGModelBinding = RecyclerItemModelGBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(recyclerGModelBinding.root, recyclerGModelBinding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val image: Int = if (class_[position] == "") {

                if (sp.getBoolean("pref_dark", false)) R.drawable.ic_gaika_light
                else R.drawable.ic_gaika_dark
            } else {

                if (sp.getBoolean("pref_dark", false)) R.drawable.ic_bolt_light
                else R.drawable.ic_bolt_dark
            }

            val g = ModelG(
                id[position],
                desc1[position],
                desc2[position],
                threadPitch[position],
                threadPer[position],
                class_[position],
                exMajorDiaMax[position],
                exMajorDiaMin[position],
                exPitchDiaMax[position],
                exPitchDiaMin[position],
                exMinorDiaMax[position],
                inMinorDiaMin[position],
                inMinorDiaMax[position],
                inPitchDiaMax[position],
                inPitchDiaMin[position],
                inMajorDiaMin[position],
                inTapDrill[position],
                image
            )
            holder.setData(g)
            holder.itemView.setOnClickListener { mListener.onGSelected(g) }
        }
    }

    interface OnGSelected {
        fun onGSelected(modelG: ModelG)
    }

    //==============================
    internal inner class ViewHolder constructor(
        itemView: View,
        private val recyclerItemGListBinding: RecyclerItemModelGBinding
    ) : RecyclerView.ViewHolder(itemView) {

        fun setData(modelG: ModelG) {
            recyclerItemGListBinding.modelG = modelG
        }
    }
}