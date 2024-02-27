package com.hfad.myferma.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.hfad.myferma.db.MyConstanta.DAYDAMP1
import com.hfad.myferma.db.MyConstanta.DAYDAMP10
import com.hfad.myferma.db.MyConstanta.DAYDAMP11
import com.hfad.myferma.db.MyConstanta.DAYDAMP12
import com.hfad.myferma.db.MyConstanta.DAYDAMP13
import com.hfad.myferma.db.MyConstanta.DAYDAMP14
import com.hfad.myferma.db.MyConstanta.DAYDAMP15
import com.hfad.myferma.db.MyConstanta.DAYDAMP16
import com.hfad.myferma.db.MyConstanta.DAYDAMP17
import com.hfad.myferma.db.MyConstanta.DAYDAMP18
import com.hfad.myferma.db.MyConstanta.DAYDAMP19
import com.hfad.myferma.db.MyConstanta.DAYDAMP2
import com.hfad.myferma.db.MyConstanta.DAYDAMP20
import com.hfad.myferma.db.MyConstanta.DAYDAMP21
import com.hfad.myferma.db.MyConstanta.DAYDAMP22
import com.hfad.myferma.db.MyConstanta.DAYDAMP23
import com.hfad.myferma.db.MyConstanta.DAYDAMP24
import com.hfad.myferma.db.MyConstanta.DAYDAMP25
import com.hfad.myferma.db.MyConstanta.DAYDAMP26
import com.hfad.myferma.db.MyConstanta.DAYDAMP27
import com.hfad.myferma.db.MyConstanta.DAYDAMP28
import com.hfad.myferma.db.MyConstanta.DAYDAMP29
import com.hfad.myferma.db.MyConstanta.DAYDAMP3
import com.hfad.myferma.db.MyConstanta.DAYDAMP30
import com.hfad.myferma.db.MyConstanta.DAYDAMP4
import com.hfad.myferma.db.MyConstanta.DAYDAMP5
import com.hfad.myferma.db.MyConstanta.DAYDAMP6
import com.hfad.myferma.db.MyConstanta.DAYDAMP7
import com.hfad.myferma.db.MyConstanta.DAYDAMP8
import com.hfad.myferma.db.MyConstanta.DAYDAMP9
import com.hfad.myferma.db.MyConstanta.DAYTEMP1
import com.hfad.myferma.db.MyConstanta.DAYTEMP10
import com.hfad.myferma.db.MyConstanta.DAYTEMP11
import com.hfad.myferma.db.MyConstanta.DAYTEMP12
import com.hfad.myferma.db.MyConstanta.DAYTEMP13
import com.hfad.myferma.db.MyConstanta.DAYTEMP14
import com.hfad.myferma.db.MyConstanta.DAYTEMP15
import com.hfad.myferma.db.MyConstanta.DAYTEMP16
import com.hfad.myferma.db.MyConstanta.DAYTEMP17
import com.hfad.myferma.db.MyConstanta.DAYTEMP18
import com.hfad.myferma.db.MyConstanta.DAYTEMP19
import com.hfad.myferma.db.MyConstanta.DAYTEMP2
import com.hfad.myferma.db.MyConstanta.DAYTEMP20
import com.hfad.myferma.db.MyConstanta.DAYTEMP21
import com.hfad.myferma.db.MyConstanta.DAYTEMP22
import com.hfad.myferma.db.MyConstanta.DAYTEMP23
import com.hfad.myferma.db.MyConstanta.DAYTEMP24
import com.hfad.myferma.db.MyConstanta.DAYTEMP25
import com.hfad.myferma.db.MyConstanta.DAYTEMP26
import com.hfad.myferma.db.MyConstanta.DAYTEMP27
import com.hfad.myferma.db.MyConstanta.DAYTEMP28
import com.hfad.myferma.db.MyConstanta.DAYTEMP29
import com.hfad.myferma.db.MyConstanta.DAYTEMP3
import com.hfad.myferma.db.MyConstanta.DAYTEMP30
import com.hfad.myferma.db.MyConstanta.DAYTEMP4
import com.hfad.myferma.db.MyConstanta.DAYTEMP5
import com.hfad.myferma.db.MyConstanta.DAYTEMP6
import com.hfad.myferma.db.MyConstanta.DAYTEMP7
import com.hfad.myferma.db.MyConstanta.DAYTEMP8
import com.hfad.myferma.db.MyConstanta.DAYTEMP9
import java.util.Calendar

class MyFermaDatabaseHelper constructor(private val context: Context) : SQLiteOpenHelper(
    context, MyConstanta.DB_NAME, null, MyConstanta.DB_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(MyConstanta.TABLE_STRUCTURE)
        db.execSQL(MyConstanta.TABLE_STRUCTURESale)
        db.execSQL(MyConstanta.TABLE_STRUCTUREEXPENSES)
        db.execSQL(MyConstanta.TABLE_STRUCTUREPRICE)
        db.execSQL(MyConstanta.TABLE_STRUCTUREWRITEOFF)
        db.execSQL(MyConstanta.TABLE_STRUCTUREINCUBATOR)
        db.execSQL(MyConstanta.TABLE_STRUCTUREINCUBATORTEMP)
        db.execSQL(MyConstanta.TABLE_STRUCTUREINCUBATORDAMP)
        db.execSQL(MyConstanta.TABLE_STRUCTUREINCUBATOROVER)
        db.execSQL(MyConstanta.TABLE_STRUCTUREINCUBATORAIRING)
        db.execSQL(MyConstanta.TABLE_STRUCTUREPRODUCT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w(
            MyFermaDatabaseHelper::class.java.name,
            ("Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data")
        )
        if (oldVersion<8){
            db.execSQL(MyConstanta.TABLE_STRUCTUREINCUBATORTEMP)
            db.execSQL(MyConstanta.TABLE_STRUCTUREINCUBATORDAMP)

            db.execSQL("INSERT INTO ${MyConstanta.TABLE_INCUBATORTEMP} ($DAYTEMP1, $DAYTEMP2, $DAYTEMP3, $DAYTEMP4, $DAYTEMP5, $DAYTEMP6, $DAYTEMP7, $DAYTEMP8, $DAYTEMP9, $DAYTEMP10, $DAYTEMP11, $DAYTEMP12, $DAYTEMP13, $DAYTEMP14, $DAYTEMP15, $DAYTEMP16, $DAYTEMP17, $DAYTEMP18, $DAYTEMP19, $DAYTEMP20, $DAYTEMP21, $DAYTEMP22, $DAYTEMP23, $DAYTEMP24, $DAYTEMP25, $DAYTEMP26, $DAYTEMP27, $DAYTEMP28, $DAYTEMP29, $DAYTEMP30) " +
                    "SELECT $DAYTEMP1, $DAYTEMP2, $DAYTEMP3, $DAYTEMP4, $DAYTEMP5, $DAYTEMP6, $DAYTEMP7, $DAYTEMP8, $DAYTEMP9, $DAYTEMP10, $DAYTEMP11, $DAYTEMP12, $DAYTEMP13, $DAYTEMP14, $DAYTEMP15, $DAYTEMP16, $DAYTEMP17, $DAYTEMP18, $DAYTEMP19, $DAYTEMP20, $DAYTEMP21, $DAYTEMP22, $DAYTEMP23, $DAYTEMP24, $DAYTEMP25, $DAYTEMP26, $DAYTEMP27, $DAYTEMP28, $DAYTEMP29, $DAYTEMP30 FROM ${MyConstanta.TABLE_INCUBATORTEMPDAMP}")

            db.execSQL("INSERT INTO ${MyConstanta.TABLE_INCUBATORDAMP} ($DAYDAMP1, $DAYDAMP2, $DAYDAMP3, $DAYDAMP4, $DAYDAMP5, $DAYDAMP6, $DAYDAMP7, $DAYDAMP8, $DAYDAMP9, $DAYDAMP10, $DAYDAMP11, $DAYDAMP12, $DAYDAMP13, $DAYDAMP14, $DAYDAMP15, $DAYDAMP16, $DAYDAMP17, $DAYDAMP18, $DAYDAMP19, $DAYDAMP20, $DAYDAMP21, $DAYDAMP22, $DAYDAMP23, $DAYDAMP24, $DAYDAMP25, $DAYDAMP26, $DAYDAMP27, $DAYDAMP28, $DAYDAMP29, $DAYDAMP30) " +
                    "SELECT $DAYDAMP1, $DAYDAMP2, $DAYDAMP3, $DAYDAMP4, $DAYDAMP5, $DAYDAMP6, $DAYDAMP7, $DAYDAMP8, $DAYDAMP9, $DAYDAMP10, $DAYDAMP11, $DAYDAMP12, $DAYDAMP13, $DAYDAMP14, $DAYDAMP15, $DAYDAMP16, $DAYDAMP17, $DAYDAMP18, $DAYDAMP19, $DAYDAMP20, $DAYDAMP21, $DAYDAMP22, $DAYDAMP23, $DAYDAMP24, $DAYDAMP25, $DAYDAMP26, $DAYDAMP27, $DAYDAMP28, $DAYDAMP29, $DAYDAMP30 FROM ${MyConstanta.TABLE_INCUBATORTEMPDAMP}")

            db.execSQL(MyConstanta.DROP_TABLEINCUBATORTEMPDAMP)
        }


        onCreate(db)
    }

    fun deleteDatabase(context: Context): Boolean {
        return context.deleteDatabase(MyConstanta.DB_NAME)
    }

    fun readAllData(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM " + MyConstanta.TABLE_NAME, null)
    }

    fun readAllDataSale(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM " + MyConstanta.TABLE_NAMESALE, null)
    }

    fun readAllDataExpenses(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM " + MyConstanta.TABLE_NAMEEXPENSES, null)
    }

    //Список купленых товаров без повторов
    fun readDataExpensesGroup(): Cursor {
        val db = readableDatabase
        return db.rawQuery(
            "SELECT * FROM ${MyConstanta.TABLE_NAMEEXPENSES} group by ${MyConstanta.TITLEEXPENSES}",
            null
        )
    }

    //Сумма заработаных денег и потраченых
    fun readDataAllSumTable(priceColumn: String, nameTable: String): Cursor {
        val db = readableDatabase
        return db.rawQuery(
            "SELECT sum($priceColumn)" +
                    " FROM $nameTable",
            null
        )
    }

    fun readAllDataWriteOff(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM " + MyConstanta.TABLE_NAMEWRITEOFF, null)
    }

    fun readAllDataProduct(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM " + MyConstanta.TABLE_NAMEPRODUCT, null)
    }

    fun readAllDataIncubator(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM " + MyConstanta.TABLE_INCUBATOR, null)
    }

    fun readAllDataIncubatorTempDamp(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM " + MyConstanta.TABLE_INCUBATORTEMPDAMP, null)
    }

    fun readAllDataIncubatorOver(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM " + MyConstanta.TABLE_INCUBATOROVER, null)
    }

    fun readAllDataIncubatorAiring(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM " + MyConstanta.TABLE_INCUBATORAIRING, null)
    }

    fun readAllDataPrice(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM " + MyConstanta.TABLE_NAMEPRICE, null)
    }

    fun dataPrice(nameProduct: String): Cursor {
        val db = readableDatabase
        return db.query(
            MyConstanta.TABLE_NAMEPRICE,
            null,
            MyConstanta.TITLEPRISE + " = ?", arrayOf(nameProduct),
            null, null, null
        )
    }

    //Выдает кол-во товара в таблице
    fun idProduct1(nameTable: String, nameCount: String, nameProduct: String): Cursor {
        val db = readableDatabase
        return db.query(
            nameTable,
            null,
            "$nameCount = ?", arrayOf(nameProduct),
            null, null, null
        )
    }

    //todo возможно будет через бай групп
    fun selectTableNameAndSumCount(
        nameTable: String,
        titleСolumn: String,
        nameProduct: String,
        countColumn: String,
    ): Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT $titleСolumn, sum($countColumn)" +
                    " FROM $nameTable " +
                    " WHERE $titleСolumn = ?",
            arrayOf(nameProduct)
        )
    }

    //Эксперименты TODO КОсяк возможно
    fun selectChartMount(
        discrotionСolumn: String,
        nameTable: String,
        typeColumn: String,
        type: String,
        mount: String,
        year: String
        ): Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT sum($discrotionСolumn), ${MyConstanta.DAY}" +
                    " FROM $nameTable " +
                    " WHERE $typeColumn = ?  and  ${MyConstanta.MOUNT} =?  and ${MyConstanta.YEAR} = ?" +
                    " group by ${MyConstanta.DAY}",
            arrayOf(type, mount, year)
        )
    }

    fun selectChartYear(
        discrotionСolumn: String,
        nameTable: String,
        typeColumn: String,
        type: String,
        year: String
    ): Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT sum($discrotionСolumn), ${MyConstanta.MOUNT}" +
                    " FROM $nameTable " +
                    " WHERE $typeColumn = ?  and ${MyConstanta.YEAR} = ?" +
                    " group by ${MyConstanta.MOUNT}",
            arrayOf(type, year)
        )
    }

    //Эксперименты TODO КОсяк возможно
    fun selectChartMountExpen(
        mount: String,
        year: String
    ): Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT ${MyConstanta.TITLEEXPENSES}, sum(${MyConstanta.DISCROTIONEXPENSES})" +
                    " FROM ${MyConstanta.TABLE_NAMEEXPENSES}" +
                    " WHERE ${MyConstanta.MOUNT} =?  and ${MyConstanta.YEAR} = ?" +
                    " group by ${MyConstanta.TITLEEXPENSES}",
            arrayOf(mount, year)
        )
    }

    fun selectChartYearExpen(
        year: String
    ): Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT ${MyConstanta.TITLEEXPENSES}, sum(${MyConstanta.DISCROTIONEXPENSES})" +
                    " FROM ${MyConstanta.TABLE_NAMEEXPENSES}" +
                    " WHERE ${MyConstanta.YEAR} = ?" +
                    " group by ${MyConstanta.TITLEEXPENSES}",
            arrayOf(year)
        )
    }

    //Эксперименты TODO КОсяк возможно
    fun selectChartMountWriteOff(
        type: String,
        mount: String,
        year: String,
        status: String
    ): Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT sum(${MyConstanta.TITLEWRITEOFF}), ${MyConstanta.DAY}" +
                    " FROM ${MyConstanta.TABLE_NAMEWRITEOFF} " +
                    " WHERE ${MyConstanta.TITLEWRITEOFF} = ?  and  ${MyConstanta.MOUNT} =?  and ${MyConstanta.YEAR} = ? and ${MyConstanta.STASTUSWRITEOFF}" +
                    " group by ${MyConstanta.DAY}",
            arrayOf(type, mount, year, status)
        )
    }

    fun selectChartYearWriteOff(
        type: String,
        year: String,
        status: String
    ): Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT sum(${MyConstanta.TITLEWRITEOFF}), ${MyConstanta.MOUNT}" +
                    " FROM ${MyConstanta.TABLE_NAMEWRITEOFF} " +
                    " WHERE ${MyConstanta.TITLEWRITEOFF} = ? and ${MyConstanta.YEAR} = ? and ${MyConstanta.STASTUSWRITEOFF}" +
                    " group by ${MyConstanta.MOUNT}",
            arrayOf(type, year, status)
        )
    }


    //Эксперименты TODO КОсяк возможно
    fun selectChartMountFinance1(
        mount: String,
        year: String
    ): Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT ${MyConstanta.TITLESale}, sum(${MyConstanta.PRICEALL}), ${MyConstanta.DAY}" +
                    " FROM ${MyConstanta.TABLE_NAMESALE} " +
                    " WHERE ${MyConstanta.MOUNT} =?  and ${MyConstanta.YEAR} = ?" +
                    " group by ${MyConstanta.TITLESale} and ${MyConstanta.DAY}",
            arrayOf(mount, year)
        )
    }

//    fun selectChartYear(
//        discrotionСolumn: String,
//        nameTable: String,
//        typeColumn: String,
//        type: String,
//        year: String
//    ): Cursor {
//        val db = this.readableDatabase
//        return db.rawQuery(
//            "SELECT sum($discrotionСolumn), ${MyConstanta.MOUNT}" +
//                    " FROM $nameTable " +
//                    " WHERE $typeColumn = ?  and ${MyConstanta.YEAR} = ?" +
//                    " group by ${MyConstanta.MOUNT}",
//            arrayOf(type, year)
//        )
//    }


    fun idIncubator(id: String): Cursor {
        val db = readableDatabase
        return db.query(
            MyConstanta.TABLE_INCUBATOR,
            null,
            "_id = ?", arrayOf(id),
            null, null, null
        )
    }

    fun idIncubatorTemp(id: String): Cursor {
        val db = readableDatabase
        return db.query(
            MyConstanta.TABLE_INCUBATORTEMP,
            null,
            "_id = ?", arrayOf(id),
            null, null, null
        )
    }

    fun idIncubatorDamp(id: String): Cursor {
        val db = readableDatabase
        return db.query(
            MyConstanta.TABLE_INCUBATORDAMP,
            null,
            "_id = ?", arrayOf(id),
            null, null, null
        )
    }

    fun idIncubatorOver(id: String): Cursor {
        val db = readableDatabase
        return db.query(
            MyConstanta.TABLE_INCUBATOROVER,
            null,
            "_id = ?", arrayOf(id),
            null, null, null
        )
    }

    fun idIncubatorAiring(id: String): Cursor {
        val db = readableDatabase
        return db.query(
            MyConstanta.TABLE_INCUBATORAIRING,
            null,
            "_id = ?", arrayOf(id),
            null, null, null
        )
    }

    fun insertDataProduct(title: String, product: Int) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(MyConstanta.TITLEPRODUCT, title)
        cv.put(MyConstanta.STATUSPRODUCT, product)
        db.insert(MyConstanta.TABLE_NAMEPRODUCT, null, cv)
    }

    fun insertToDb(title: String, disc: Double) {
        val db = writableDatabase
        val cv = ContentValues()
        val calendar = Calendar.getInstance()
        //        double priceAdd = disc * price(title); // todo цена!!
        cv.put(MyConstanta.TITLE, title)
        cv.put(MyConstanta.DISCROTION, disc)
        cv.put(MyConstanta.DAY, calendar.get(Calendar.DAY_OF_MONTH))
        cv.put(MyConstanta.MOUNT, calendar.get(Calendar.MONTH) + 1)
        cv.put(MyConstanta.YEAR, calendar.get(Calendar.YEAR))
        cv.put(MyConstanta.PRICEALL, 0)
        db.insert(MyConstanta.TABLE_NAME, null, cv)
    }

    fun insertToDbSale(title: String, disc: Double, priceIndi: Double) {
        val db = writableDatabase
        val calendar = Calendar.getInstance()
        val cv = ContentValues()
        cv.put(MyConstanta.TITLESale, title)
        cv.put(MyConstanta.DISCROTIONSale, disc)
        cv.put(MyConstanta.DAY, calendar.get(Calendar.DAY_OF_MONTH))
        cv.put(MyConstanta.MOUNT, calendar.get(Calendar.MONTH) + 1)
        cv.put(MyConstanta.YEAR, calendar.get(Calendar.YEAR))
        cv.put(MyConstanta.PRICEALL, priceIndi)
        db.insert(MyConstanta.TABLE_NAMESALE, null, cv)
    }

    fun insertToDbExpenses(title: String, disc: Double) {
        val db = writableDatabase
        val calendar = Calendar.getInstance()
        val cv = ContentValues()
        cv.put(MyConstanta.TITLEEXPENSES, title)
        cv.put(MyConstanta.DISCROTIONEXPENSES, disc)
        cv.put(MyConstanta.DAY, calendar.get(Calendar.DAY_OF_MONTH))
        cv.put(MyConstanta.MOUNT, calendar.get(Calendar.MONTH) + 1)
        cv.put(MyConstanta.YEAR, calendar.get(Calendar.YEAR))
        db.insert(MyConstanta.TABLE_NAMEEXPENSES, null, cv)
    }

    fun insertToDbWriteOff(title: String, disc: Double, status: Int) {
        val db = writableDatabase
        val cv = ContentValues()
        val calendar = Calendar.getInstance()
        cv.put(MyConstanta.TITLEWRITEOFF, title)
        cv.put(MyConstanta.DISCROTIONSWRITEOFF, disc)
        cv.put(MyConstanta.DAY, calendar.get(Calendar.DAY_OF_MONTH))
        cv.put(MyConstanta.MOUNT, calendar.get(Calendar.MONTH) + 1)
        cv.put(MyConstanta.YEAR, calendar.get(Calendar.YEAR))
        cv.put(MyConstanta.STASTUSWRITEOFF, status)
        db.insert(MyConstanta.TABLE_NAMEWRITEOFF, null, cv)
    }

    fun insertToDbPrice(title: String, disc: Double) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(MyConstanta.TITLEPRISE, title)
        cv.put(MyConstanta.DISCROTIONPRICE, disc)
        db.insert(MyConstanta.TABLE_NAMEPRICE, null, cv)
    }

    fun updateData(
        row_id: String,
        title: String,
        author: String,
        pages: String,
        mount: String,
        year: String
    ) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(MyConstanta.TITLE, title)
        cv.put(MyConstanta.DISCROTION, author)
        cv.put(MyConstanta.DAY, pages)
        cv.put(MyConstanta.MOUNT, mount)
        cv.put(MyConstanta.YEAR, year)
        db.update(MyConstanta.TABLE_NAME, cv, "_id=?", arrayOf(row_id)).toLong()
    }

    fun updateDataSale(
        row_id: String,
        title: String,
        author: String,
        pages: String,
        mount: String,
        year: String,
        priceSale: Double
    ) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(MyConstanta.TITLESale, title)
        cv.put(MyConstanta.DISCROTIONSale, author)
        cv.put(MyConstanta.DAY, pages)
        cv.put(MyConstanta.MOUNT, mount)
        cv.put(MyConstanta.YEAR, year)
        cv.put(MyConstanta.PRICEALL, priceSale)
        db.update(MyConstanta.TABLE_NAMESALE, cv, "_id=?", arrayOf(row_id)).toLong()
    }

    fun updateDataExpenses(
        row_id: String,
        title: String,
        author: String,
        pages: String,
        mount: String,
        year: String
    ) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(MyConstanta.TITLEEXPENSES, title)
        cv.put(MyConstanta.DISCROTIONEXPENSES, author)
        cv.put(MyConstanta.DAY, pages)
        cv.put(MyConstanta.MOUNT, mount)
        cv.put(MyConstanta.YEAR, year)
        db.update(MyConstanta.TABLE_NAMEEXPENSES, cv, "_id=?", arrayOf(row_id)).toLong()
    }

    fun updateDataWriteOff(
        row_id: String,
        title: String,
        author: String,
        pages: String,
        mount: String,
        year: String,
        status: Int
    ) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(MyConstanta.TITLEWRITEOFF, title)
        cv.put(MyConstanta.DISCROTIONSWRITEOFF, author)
        cv.put(MyConstanta.DAY, pages)
        cv.put(MyConstanta.MOUNT, mount)
        cv.put(MyConstanta.YEAR, year)
        cv.put(MyConstanta.STASTUSWRITEOFF, status)
        db.update(MyConstanta.TABLE_NAMEWRITEOFF, cv, "_id=?", arrayOf(row_id)).toLong()

    }

    fun updateDataProduct(row_id: String, title: String, product: Int) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(MyConstanta.TITLEPRODUCT, title)
        cv.put(MyConstanta.STATUSPRODUCT, product)
        db.update(MyConstanta.TABLE_NAMEPRODUCT, cv, "_id=?", arrayOf(row_id)).toLong()

    }

    fun updateDataPrice(title: String, product: Double) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(MyConstanta.TITLEPRISE, title)
        cv.put(MyConstanta.DISCROTIONPRICE, product)
        db.update(
            MyConstanta.TABLE_NAMEPRICE,
            cv,
            MyConstanta.TITLEPRISE + " =?",
            arrayOf(title)
        )
    }

    fun deleteAllData() {
        val db = writableDatabase
        db.execSQL("DELETE FROM " + MyConstanta.TABLE_NAME)
    }

    fun deleteAllIncubator() {
        val db = writableDatabase
        db.execSQL("DELETE FROM " + MyConstanta.TABLE_INCUBATOR)
        db.execSQL("DELETE FROM " + MyConstanta.TABLE_INCUBATORTEMPDAMP)
        db.execSQL("DELETE FROM " + MyConstanta.TABLE_INCUBATOROVER)
        db.execSQL("DELETE FROM " + MyConstanta.TABLE_INCUBATORAIRING)
    }

    fun deleteOneRow(row_id: String) {
        val db = writableDatabase
        db.delete(MyConstanta.TABLE_NAME, "_id=?", arrayOf(row_id))
    }

    fun deleteOneRowSale(row_id: String) {
        val db = writableDatabase
        db.delete(MyConstanta.TABLE_NAMESALE, "_id=?", arrayOf(row_id))
    }

    fun deleteOneRowExpenses(row_id: String) {
        val db = writableDatabase
        db.delete(MyConstanta.TABLE_NAMEEXPENSES, "_id=?", arrayOf(row_id))
    }

    fun deleteOneRowWriteOff(row_id: String) {
        val db = writableDatabase
        db.delete(MyConstanta.TABLE_NAMEWRITEOFF, "_id=?", arrayOf(row_id))
    }

    fun deleteOneRowProduct(row_id: String) {
        val db = writableDatabase
        db.delete(MyConstanta.TABLE_NAMEPRICE, "titlePRICE=?", arrayOf(row_id))
        db.delete(MyConstanta.TABLE_NAMEPRODUCT, "Product=?", arrayOf(row_id))
    }

    fun deleteOneRowIncubator(row_id: String) {
        val db = writableDatabase
        db.delete(MyConstanta.TABLE_INCUBATOR, "_id=?", arrayOf(row_id)).toLong()
        db.delete(MyConstanta.TABLE_INCUBATORTEMPDAMP, "_id=?", arrayOf(row_id))
        db.delete(MyConstanta.TABLE_INCUBATOROVER, "_id=?", arrayOf(row_id))
        db.delete(MyConstanta.TABLE_INCUBATORAIRING, "_id=?", arrayOf(row_id))
    }

    fun insertToDbIncubator(
        name: String,
        type: String,
        data: String,
        overturn: String,
        eggAll: String,
        eggEndAll: String,
        airing: String,
        arhive: String,
        dataEnd: String,
        timePush1: String,
        timePush2: String,
        timePush3: String
    ) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(MyConstanta.NAMEINCUBATOR, name)
        cv.put(MyConstanta.TYPEINCUBATOR, type)
        cv.put(MyConstanta.DATAINCUBATOR, data)
        cv.put(MyConstanta.OVERTURNINCUBATOR, overturn)
        cv.put(MyConstanta.EGGALL, eggAll)
        cv.put(MyConstanta.EGGALLEND, eggEndAll)
        cv.put(MyConstanta.AIRING, airing)
        cv.put(MyConstanta.ARHIVE, arhive)
        cv.put(MyConstanta.DATAEND, dataEnd)
        cv.put(MyConstanta.TIMEPUSH1, timePush1)
        cv.put(MyConstanta.TIMEPUSH2, timePush2)
        cv.put(MyConstanta.TIMEPUSH3, timePush3)
        db.insert(MyConstanta.TABLE_INCUBATOR, null, cv)
    }


    fun insertToDbIncubatorTemp(
        day: MutableList<String>
    ) {
        val db = writableDatabase
        val cv: ContentValues = ContentValues()
        cv.put(MyConstanta.DAYTEMP1, day[0])
        cv.put(MyConstanta.DAYTEMP2, day[1])
        cv.put(MyConstanta.DAYTEMP3, day[2])
        cv.put(MyConstanta.DAYTEMP4, day[3])
        cv.put(MyConstanta.DAYTEMP5, day[4])
        cv.put(MyConstanta.DAYTEMP6, day[5])
        cv.put(MyConstanta.DAYTEMP7, day[6])
        cv.put(MyConstanta.DAYTEMP8, day[7])
        cv.put(MyConstanta.DAYTEMP9, day[8])
        cv.put(MyConstanta.DAYTEMP10, day[9])
        cv.put(MyConstanta.DAYTEMP11, day[10])
        cv.put(MyConstanta.DAYTEMP12, day[11])
        cv.put(MyConstanta.DAYTEMP13, day[12])
        cv.put(MyConstanta.DAYTEMP14, day[13])
        cv.put(MyConstanta.DAYTEMP15, day[14])
        cv.put(MyConstanta.DAYTEMP16, day[15])
        cv.put(MyConstanta.DAYTEMP17, day[16])
        cv.put(MyConstanta.DAYTEMP18, day[17])
        cv.put(MyConstanta.DAYTEMP19, day[18])
        cv.put(MyConstanta.DAYTEMP20, day[19])
        cv.put(MyConstanta.DAYTEMP21, day[20])
        cv.put(MyConstanta.DAYTEMP22, day[21])
        cv.put(MyConstanta.DAYTEMP23, day[22])
        cv.put(MyConstanta.DAYTEMP24, day[23])
        cv.put(MyConstanta.DAYTEMP25, day[24])
        cv.put(MyConstanta.DAYTEMP26, day[25])
        cv.put(MyConstanta.DAYTEMP27, day[26])
        cv.put(MyConstanta.DAYTEMP28, day[27])
        cv.put(MyConstanta.DAYTEMP29, day[28])
        cv.put(MyConstanta.DAYTEMP30, day[29])
        db.insert(MyConstanta.TABLE_INCUBATORTEMP, null, cv)
    }

    fun insertToDbIncubatorDamp(
        day2: MutableList<String>
    ) {
        val db = writableDatabase
        val cv: ContentValues = ContentValues()
        cv.put(MyConstanta.DAYDAMP1, day2[0])
        cv.put(MyConstanta.DAYDAMP2, day2[1])
        cv.put(MyConstanta.DAYDAMP3, day2[2])
        cv.put(MyConstanta.DAYDAMP4, day2[3])
        cv.put(MyConstanta.DAYDAMP5, day2[4])
        cv.put(MyConstanta.DAYDAMP6, day2[5])
        cv.put(MyConstanta.DAYDAMP7, day2[6])
        cv.put(MyConstanta.DAYDAMP8, day2[7])
        cv.put(MyConstanta.DAYDAMP9, day2[8])
        cv.put(MyConstanta.DAYDAMP10, day2[9])
        cv.put(MyConstanta.DAYDAMP11, day2[10])
        cv.put(MyConstanta.DAYDAMP12, day2[11])
        cv.put(MyConstanta.DAYDAMP13, day2[12])
        cv.put(MyConstanta.DAYDAMP14, day2[13])
        cv.put(MyConstanta.DAYDAMP15, day2[14])
        cv.put(MyConstanta.DAYDAMP16, day2[15])
        cv.put(MyConstanta.DAYDAMP17, day2[16])
        cv.put(MyConstanta.DAYDAMP18, day2[17])
        cv.put(MyConstanta.DAYDAMP19, day2[18])
        cv.put(MyConstanta.DAYDAMP20, day2[19])
        cv.put(MyConstanta.DAYDAMP21, day2[20])
        cv.put(MyConstanta.DAYDAMP22, day2[21])
        cv.put(MyConstanta.DAYDAMP23, day2[22])
        cv.put(MyConstanta.DAYDAMP24, day2[23])
        cv.put(MyConstanta.DAYDAMP25, day2[24])
        cv.put(MyConstanta.DAYDAMP26, day2[25])
        cv.put(MyConstanta.DAYDAMP27, day2[26])
        cv.put(MyConstanta.DAYDAMP28, day2[27])
        cv.put(MyConstanta.DAYDAMP29, day2[28])
        cv.put(MyConstanta.DAYDAMP30, day2[29])
        db.insert(MyConstanta.TABLE_INCUBATORDAMP, null, cv)
    }


    fun insertToDbIncubatorOver(
        day: MutableList<String>
    ) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(MyConstanta.DAYOVERTURN1, day[0])
        cv.put(MyConstanta.DAYOVERTURN2, day[1])
        cv.put(MyConstanta.DAYOVERTURN3, day[2])
        cv.put(MyConstanta.DAYOVERTURN4, day[3])
        cv.put(MyConstanta.DAYOVERTURN5, day[4])
        cv.put(MyConstanta.DAYOVERTURN6, day[5])
        cv.put(MyConstanta.DAYOVERTURN7, day[6])
        cv.put(MyConstanta.DAYOVERTURN8, day[7])
        cv.put(MyConstanta.DAYOVERTURN9, day[8])
        cv.put(MyConstanta.DAYOVERTURN10, day[9])
        cv.put(MyConstanta.DAYOVERTURN11, day[10])
        cv.put(MyConstanta.DAYOVERTURN12, day[11])
        cv.put(MyConstanta.DAYOVERTURN13, day[12])
        cv.put(MyConstanta.DAYOVERTURN14, day[13])
        cv.put(MyConstanta.DAYOVERTURN15, day[14])
        cv.put(MyConstanta.DAYOVERTURN16, day[15])
        cv.put(MyConstanta.DAYOVERTURN17, day[16])
        cv.put(MyConstanta.DAYOVERTURN18, day[17])
        cv.put(MyConstanta.DAYOVERTURN19, day[18])
        cv.put(MyConstanta.DAYOVERTURN20, day[19])
        cv.put(MyConstanta.DAYOVERTURN21, day[20])
        cv.put(MyConstanta.DAYOVERTURN22, day[21])
        cv.put(MyConstanta.DAYOVERTURN23, day[22])
        cv.put(MyConstanta.DAYOVERTURN24, day[23])
        cv.put(MyConstanta.DAYOVERTURN25, day[24])
        cv.put(MyConstanta.DAYOVERTURN26, day[25])
        cv.put(MyConstanta.DAYOVERTURN27, day[26])
        cv.put(MyConstanta.DAYOVERTURN28, day[27])
        cv.put(MyConstanta.DAYOVERTURN29, day[28])
        cv.put(MyConstanta.DAYOVERTURN30, day[29])
        db.insert(MyConstanta.TABLE_INCUBATOROVER, null, cv)
    }

    fun insertToDbIncubatorAiring(
        day: MutableList<String>
    ) {
        val db = writableDatabase
        val cv: ContentValues = ContentValues()
        cv.put(MyConstanta.DAYAIRING1, day[0])
        cv.put(MyConstanta.DAYAIRING2, day[1])
        cv.put(MyConstanta.DAYAIRING3, day[2])
        cv.put(MyConstanta.DAYAIRING4, day[3])
        cv.put(MyConstanta.DAYAIRING5, day[4])
        cv.put(MyConstanta.DAYAIRING6, day[5])
        cv.put(MyConstanta.DAYAIRING7, day[6])
        cv.put(MyConstanta.DAYAIRING8, day[7])
        cv.put(MyConstanta.DAYAIRING9, day[8])
        cv.put(MyConstanta.DAYAIRING10, day[9])
        cv.put(MyConstanta.DAYAIRING11, day[10])
        cv.put(MyConstanta.DAYAIRING12, day[11])
        cv.put(MyConstanta.DAYAIRING13, day[12])
        cv.put(MyConstanta.DAYAIRING14, day[13])
        cv.put(MyConstanta.DAYAIRING15, day[14])
        cv.put(MyConstanta.DAYAIRING16, day[15])
        cv.put(MyConstanta.DAYAIRING17, day[16])
        cv.put(MyConstanta.DAYAIRING18, day[17])
        cv.put(MyConstanta.DAYAIRING19, day[18])
        cv.put(MyConstanta.DAYAIRING20, day[19])
        cv.put(MyConstanta.DAYAIRING21, day[20])
        cv.put(MyConstanta.DAYAIRING22, day[21])
        cv.put(MyConstanta.DAYAIRING23, day[22])
        cv.put(MyConstanta.DAYAIRING24, day[23])
        cv.put(MyConstanta.DAYAIRING25, day[24])
        cv.put(MyConstanta.DAYAIRING26, day[25])
        cv.put(MyConstanta.DAYAIRING27, day[26])
        cv.put(MyConstanta.DAYAIRING28, day[27])
        cv.put(MyConstanta.DAYAIRING29, day[28])
        cv.put(MyConstanta.DAYAIRING30, day[29])
        db.insert(MyConstanta.TABLE_INCUBATORAIRING, null, cv)
    }

    fun updateIncubator(mass: MutableList<String>) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(MyConstanta.NAMEINCUBATOR, mass[1])
        cv.put(MyConstanta.TYPEINCUBATOR, mass[2])
        cv.put(MyConstanta.DATAINCUBATOR, mass[3])
        cv.put(MyConstanta.OVERTURNINCUBATOR, mass[4])
        cv.put(MyConstanta.EGGALL, mass[5])
        cv.put(MyConstanta.EGGALLEND, mass[6])
        cv.put(MyConstanta.AIRING, mass[7])
        cv.put(MyConstanta.ARHIVE, mass[8])
        cv.put(MyConstanta.DATAEND, mass[9])
        cv.put(MyConstanta.TIMEPUSH1, mass[10])
        cv.put(MyConstanta.TIMEPUSH2, mass[11])
        cv.put(MyConstanta.TIMEPUSH3, mass[12])
        db.update(MyConstanta.TABLE_INCUBATOR, cv, "_id=?", arrayOf(mass.get(0)))
    }

    fun updateIncubatorTemp(mass: MutableList<String>, id: String) {
        val db: SQLiteDatabase = writableDatabase
        val cv: ContentValues = ContentValues()
        cv.put(MyConstanta.DAYTEMP1, mass[0])
        cv.put(MyConstanta.DAYTEMP2, mass[2])
        cv.put(MyConstanta.DAYTEMP3, mass[2])
        cv.put(MyConstanta.DAYTEMP4, mass[3])
        cv.put(MyConstanta.DAYTEMP5, mass[4])
        cv.put(MyConstanta.DAYTEMP6, mass[5])
        cv.put(MyConstanta.DAYTEMP7, mass[6])
        cv.put(MyConstanta.DAYTEMP8, mass[7])
        cv.put(MyConstanta.DAYTEMP9, mass[8])
        cv.put(MyConstanta.DAYTEMP10, mass[9])
        cv.put(MyConstanta.DAYTEMP11, mass[10])
        cv.put(MyConstanta.DAYTEMP12, mass[11])
        cv.put(MyConstanta.DAYTEMP13, mass[12])
        cv.put(MyConstanta.DAYTEMP14, mass[13])
        cv.put(MyConstanta.DAYTEMP15, mass[14])
        cv.put(MyConstanta.DAYTEMP16, mass[15])
        cv.put(MyConstanta.DAYTEMP17, mass[16])
        cv.put(MyConstanta.DAYTEMP18, mass[17])
        cv.put(MyConstanta.DAYTEMP19, mass[18])
        cv.put(MyConstanta.DAYTEMP20, mass[19])
        cv.put(MyConstanta.DAYTEMP21, mass[20])
        cv.put(MyConstanta.DAYTEMP22, mass[21])
        cv.put(MyConstanta.DAYTEMP23, mass[22])
        cv.put(MyConstanta.DAYTEMP24, mass[23])
        cv.put(MyConstanta.DAYTEMP25, mass[24])
        cv.put(MyConstanta.DAYTEMP26, mass[25])
        cv.put(MyConstanta.DAYTEMP27, mass[26])
        cv.put(MyConstanta.DAYTEMP28, mass[27])
        cv.put(MyConstanta.DAYTEMP29, mass[28])
        cv.put(MyConstanta.DAYTEMP30, mass[29])
        db.update(MyConstanta.TABLE_INCUBATORTEMP, cv, "_id=?", arrayOf(id))
    }


    fun updateIncubatorDamp(mass: MutableList<String>, id: String) {
        val db: SQLiteDatabase = writableDatabase
        val cv: ContentValues = ContentValues()
        cv.put(MyConstanta.DAYDAMP1, mass[0])
        cv.put(MyConstanta.DAYDAMP2, mass[1])
        cv.put(MyConstanta.DAYDAMP3, mass[2])
        cv.put(MyConstanta.DAYDAMP4, mass[3])
        cv.put(MyConstanta.DAYDAMP5, mass[4])
        cv.put(MyConstanta.DAYDAMP6, mass[5])
        cv.put(MyConstanta.DAYDAMP7, mass[6])
        cv.put(MyConstanta.DAYDAMP8, mass[7])
        cv.put(MyConstanta.DAYDAMP9, mass[8])
        cv.put(MyConstanta.DAYDAMP10, mass[9])
        cv.put(MyConstanta.DAYDAMP11, mass[10])
        cv.put(MyConstanta.DAYDAMP12, mass[11])
        cv.put(MyConstanta.DAYDAMP13, mass[12])
        cv.put(MyConstanta.DAYDAMP14, mass[13])
        cv.put(MyConstanta.DAYDAMP15, mass[14])
        cv.put(MyConstanta.DAYDAMP16, mass[15])
        cv.put(MyConstanta.DAYDAMP17, mass[16])
        cv.put(MyConstanta.DAYDAMP18, mass[17])
        cv.put(MyConstanta.DAYDAMP19, mass[18])
        cv.put(MyConstanta.DAYDAMP20, mass[19])
        cv.put(MyConstanta.DAYDAMP21, mass[20])
        cv.put(MyConstanta.DAYDAMP22, mass[21])
        cv.put(MyConstanta.DAYDAMP23, mass[22])
        cv.put(MyConstanta.DAYDAMP24, mass[23])
        cv.put(MyConstanta.DAYDAMP25, mass[24])
        cv.put(MyConstanta.DAYDAMP26, mass[25])
        cv.put(MyConstanta.DAYDAMP27, mass[26])
        cv.put(MyConstanta.DAYDAMP28, mass[27])
        cv.put(MyConstanta.DAYDAMP29, mass[28])
        cv.put(MyConstanta.DAYDAMP30, mass[29])
        db.update(MyConstanta.TABLE_INCUBATORDAMP, cv, "_id=?", arrayOf(id))
    }


    fun updateIncubatorOver(mass: MutableList<String>, id: String) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(MyConstanta.DAYOVERTURN1, mass[0])
        cv.put(MyConstanta.DAYOVERTURN2, mass[1])
        cv.put(MyConstanta.DAYOVERTURN3, mass[2])
        cv.put(MyConstanta.DAYOVERTURN4, mass[3])
        cv.put(MyConstanta.DAYOVERTURN5, mass[4])
        cv.put(MyConstanta.DAYOVERTURN6, mass[5])
        cv.put(MyConstanta.DAYOVERTURN7, mass[6])
        cv.put(MyConstanta.DAYOVERTURN8, mass[7])
        cv.put(MyConstanta.DAYOVERTURN9, mass[8])
        cv.put(MyConstanta.DAYOVERTURN10, mass[9])
        cv.put(MyConstanta.DAYOVERTURN11, mass[10])
        cv.put(MyConstanta.DAYOVERTURN12, mass[11])
        cv.put(MyConstanta.DAYOVERTURN13, mass[12])
        cv.put(MyConstanta.DAYOVERTURN14, mass[13])
        cv.put(MyConstanta.DAYOVERTURN15, mass[14])
        cv.put(MyConstanta.DAYOVERTURN16, mass[15])
        cv.put(MyConstanta.DAYOVERTURN17, mass[16])
        cv.put(MyConstanta.DAYOVERTURN18, mass[17])
        cv.put(MyConstanta.DAYOVERTURN19, mass[18])
        cv.put(MyConstanta.DAYOVERTURN20, mass[19])
        cv.put(MyConstanta.DAYOVERTURN21, mass[20])
        cv.put(MyConstanta.DAYOVERTURN22, mass[21])
        cv.put(MyConstanta.DAYOVERTURN23, mass[22])
        cv.put(MyConstanta.DAYOVERTURN24, mass[23])
        cv.put(MyConstanta.DAYOVERTURN25, mass[24])
        cv.put(MyConstanta.DAYOVERTURN26, mass[25])
        cv.put(MyConstanta.DAYOVERTURN27, mass[26])
        cv.put(MyConstanta.DAYOVERTURN28, mass[27])
        cv.put(MyConstanta.DAYOVERTURN29, mass[28])
        cv.put(MyConstanta.DAYOVERTURN30, mass[29])
        db.update(MyConstanta.TABLE_INCUBATOROVER, cv, "_id=?", arrayOf(id))
    }

    fun updateIncubatorAiring(mass: MutableList<String>, id: String) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(MyConstanta.DAYAIRING1, mass[0])
        cv.put(MyConstanta.DAYAIRING2, mass[1])
        cv.put(MyConstanta.DAYAIRING3, mass[2])
        cv.put(MyConstanta.DAYAIRING4, mass[3])
        cv.put(MyConstanta.DAYAIRING5, mass[4])
        cv.put(MyConstanta.DAYAIRING6, mass[5])
        cv.put(MyConstanta.DAYAIRING7, mass[6])
        cv.put(MyConstanta.DAYAIRING8, mass[7])
        cv.put(MyConstanta.DAYAIRING9, mass[8])
        cv.put(MyConstanta.DAYAIRING10, mass[9])
        cv.put(MyConstanta.DAYAIRING11, mass[10])
        cv.put(MyConstanta.DAYAIRING12, mass[11])
        cv.put(MyConstanta.DAYAIRING13, mass[12])
        cv.put(MyConstanta.DAYAIRING14, mass[13])
        cv.put(MyConstanta.DAYAIRING15, mass[14])
        cv.put(MyConstanta.DAYAIRING16, mass[15])
        cv.put(MyConstanta.DAYAIRING17, mass[16])
        cv.put(MyConstanta.DAYAIRING18, mass[17])
        cv.put(MyConstanta.DAYAIRING19, mass[18])
        cv.put(MyConstanta.DAYAIRING20, mass[19])
        cv.put(MyConstanta.DAYAIRING21, mass[20])
        cv.put(MyConstanta.DAYAIRING22, mass[21])
        cv.put(MyConstanta.DAYAIRING23, mass[22])
        cv.put(MyConstanta.DAYAIRING24, mass[23])
        cv.put(MyConstanta.DAYAIRING25, mass[24])
        cv.put(MyConstanta.DAYAIRING26, mass[25])
        cv.put(MyConstanta.DAYAIRING27, mass[26])
        cv.put(MyConstanta.DAYAIRING28, mass[27])
        cv.put(MyConstanta.DAYAIRING29, mass[28])
        cv.put(MyConstanta.DAYAIRING30, mass[29])
        db.update(MyConstanta.TABLE_INCUBATORAIRING, cv, "_id=?", arrayOf(id))
    }



}