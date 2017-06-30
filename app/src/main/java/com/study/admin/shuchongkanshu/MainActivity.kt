package com.study.admin.shuchongkanshu

import android.app.ListActivity
import android.content.ContentValues.TAG
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import com.study.admin.shuchongkanshu.Utils.TongzhilanUtil
import de.greenrobot.dao.query.QueryBuilder
import me.itangqi.greendao.DaoMaster
import me.itangqi.greendao.DaoSession
import me.itangqi.greendao.Note
import me.itangqi.greendao.NoteDao
import java.text.DateFormat
import java.util.*


class MainActivity : ListActivity() {
    var daoSession: DaoSession? = null
    var db: SQLiteDatabase? = null
    var daoMaster: DaoMaster? = null

    private var editText: EditText? = null
    private var cursor: Cursor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TongzhilanUtil.setStatusBar(this, false)
        setupDatabase()
        // 获取 NoteDao 对象
        getNoteDao()

        val textColumn = NoteDao.Properties.Bookname.columnName
        val orderBy = textColumn + " COLLATE LOCALIZED ASC"

        cursor = db!!.query(getNoteDao()!!.getTablename(), getNoteDao()!!.getAllColumns(), null, null, null, null, orderBy)
        val from = arrayOf(textColumn, NoteDao.Properties.Bookpath.columnName)
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)

        val adapter = SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from,
                to)
        setListAdapter(adapter)

        editText = findViewById(R.id.editTextNote) as EditText
    }

    private fun getNoteDao(): NoteDao? {
        if (daoSession != null) {
            return daoSession!!.getNoteDao()
        } else {
            return null
        }
    }

    /**
     * Button 点击的监听事件

     * @param view
     */
    fun onMyButtonClick(view: View) {
        when (view.getId()) {
            R.id.buttonAdd -> addNote()
            R.id.buttonSearch -> search()
            else -> Log.d(TAG, "what has gone wrong ?")
        }
    }
    private fun setupDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        val helper = DaoMaster.DevOpenHelper(this, "notes-db", null)
        db = helper.writableDatabase
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = DaoMaster(db)
        daoSession = daoMaster!!.newSession()
    }
    private fun addNote() {
        val noteText = editText!!.getText().toString()
        editText!!.setText("")

        val df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM)
        val comment = "Added on " + df.format(Date())

        // 插入操作，简单到只要你创建一个 Java 对象
        val note = Note(null, noteText, comment, "", Date())
        getNoteDao()!!.insert(note)
        Log.d(TAG, "Inserted new note, ID: " + note.getId())
        cursor!!.requery()
    }

    private fun search() {
        // Query 类代表了一个可以被重复执行的查询
        val query = getNoteDao()!!.queryBuilder()
                .where(NoteDao.Properties.Bookname.eq("Test1"))
                .orderAsc(NoteDao.Properties.Date)
                .build()

        //      查询结果以 List 返回
        //      List notes = query.list();
        // 在 QueryBuilder 类中内置两个 Flag 用于方便输出执行的 SQL 语句与传递参数的值
        QueryBuilder.LOG_SQL = true
        QueryBuilder.LOG_VALUES = true
    }

    /**
     * ListView 的监听事件，用于删除一个 Item
     * @param l
     * *
     * @param v
     * *
     * @param position
     * *
     * @param id
     */
    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        // 删除操作，你可以通过「id」也可以一次性删除所有
        getNoteDao()!!.deleteByKey(id)
        //        getNoteDao().deleteAll();
        Log.d(TAG, "Deleted note, ID: " + id)
        cursor!!.requery()
    }
}
