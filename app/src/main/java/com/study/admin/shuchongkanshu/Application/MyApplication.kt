package com.study.admin.shuchongkanshu.Application

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import me.itangqi.greendao.DaoMaster
import me.itangqi.greendao.DaoSession


/**
 * Created by admin on 2017/6/30.
 */
class MyApplication : Application() {
    var db: SQLiteDatabase? = null
    var daoSession: DaoSession? = null
    var daoMaster: DaoMaster? = null

    override fun onCreate() {
        super.onCreate()
        // 官方推荐将获取 DaoMaster 对象的方法放到 Application 层，这样将避免多次创建生成 Session 对象
//        setupDatabase()

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
}