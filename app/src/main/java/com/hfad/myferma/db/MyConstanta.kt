package com.hfad.myferma.db

object MyConstanta {

    const val DB_NAME = "my_db.db" //База данных
    const val DB_VERSION = 8 //Версия базы данных
    const val TABLE_NAME = "МyFerma" // Название таблицы
    const val _ID = "_id" // Индефикатор НУМЕРАЦИЯ СТРОК
    const val TITLE = "title" // Название описание (название продукта)
    const val DISCROTION = "disc" //Заголовок (кол-во)
    const val DAY = "DAY" //Заголовок (кол-во)
    const val MOUNT = "MOUNT" //Заголовок (кол-во)
    const val YEAR = "YEAR" //Заголовок (кол-во)
    const val PRICEALL = "PRICE" //Заголовок (кол-во)
    const val TABLE_NAMESALE = "МyFermaSale" // Название таблицы
    const val TITLESale =
        "titleSale" // Название описание (название продукта) название проданного товара
    const val DISCROTIONSale = "discSale" //Заголовок (кол-во) проданного товара
    const val TABLE_NAMEEXPENSES = "МyFermaEXPENSES" // Название таблицы
    const val TITLEEXPENSES =
        "titleEXPENSES" // Название описание (название продукта) название Купленого товара
    const val DISCROTIONEXPENSES = "discEXPENSES" //Заголовок (кол-во) цена товара
    const val COUNTEXPENSES = "countEXPENSES" //Заголовок кол-во товара
    const val TABLE_NAMEPRICE = "МyFermaPRICE" // Название таблицы
    const val TITLEPRISE = "titlePRICE" // Название описание (название продукта) название товара
    const val DISCROTIONPRICE = "discPRICE" //Заголовок (кол-во) цена товара
    const val TABLE_NAMEWRITEOFF = "МyFermaWRITEOFF" // Название таблицы
    const val TITLEWRITEOFF =
        "titleWRITEOFF" // Название описание (название продукта) название списанного товара
    const val DISCROTIONSWRITEOFF = "discWRITEOFF" //Заголовок (кол-во) кол-во товара
    const val STASTUSWRITEOFF = "statusWRITEOFF"
    const val TABLE_NAMEPRODUCT = "TableProduct" // Название таблицы
    const val TITLEPRODUCT =
        "Product" // Название описание (название продукта) название списанного товара
    const val STATUSPRODUCT = "Status" // Статус продукта, нужен, для отображения

    //Инкубатор
    const val TABLE_INCUBATOR = "МyINCUBATOR" // Название таблицы
    const val TABLE_INCUBATORTEMPDAMP = "МyINCUBATORTEMP" // Название таблицы старая таблица
    const val TABLE_INCUBATORTEMP = "МyINCUBATORTEMP2" // Название таблицы
    const val TABLE_INCUBATORDAMP = "МyINCUBATORTEMPDAMP" // Название таблицы

    const val TABLE_INCUBATOROVER = "МyINCUBATOROVER" // Название таблицы
    const val TABLE_INCUBATORAIRING = "МyINCUBATORAIRING" // Название таблицы
    const val NAMEINCUBATOR = "NAME" // Название инкубатора
    const val TYPEINCUBATOR = "TYPE" // Тип яиц инкубатора
    const val DATAINCUBATOR = "DATA" // Дата начала яиц инкубатора
    const val DATAEND = "DATAEND" // Дата окончания яиц инкубатора
    const val EGGALL = "EGGALL" // переворот в инкубаторе
    const val EGGALLEND = "EGGALLEND" // переворот в инкубаторе
    const val OVERTURNINCUBATOR = "OVERTURN" // переворот в инкубаторе
    const val AIRING = "AIRING" // Охлаждение в инкубаторе
    const val ARHIVE = "ARHIVE" // Статус в архиве
    const val TIMEPUSH1 = "TIMEPUSH1" // Статус в архиве
    const val TIMEPUSH2 = "TIMEPUSH2" // Статус в архиве
    const val TIMEPUSH3 = "TIMEPUSH3" // Статус в архиве

    //Температура
    const val DAYTEMP1 = "DAYTEMP1" // Температура в инкубаторе день 1
    const val DAYTEMP2 = "DAYTEMP2" // Температура в инкубаторе день 1
    const val DAYTEMP3 = "DAYTEMP3" // Температура в инкубаторе день 1
    const val DAYTEMP4 = "DAYTEMP4" // Температура в инкубаторе день 1
    const val DAYTEMP5 = "DAYTEMP5" // Температура в инкубаторе день 1
    const val DAYTEMP6 = "DAYTEMP6" // Температура в инкубаторе день 1
    const val DAYTEMP7 = "DAYTEMP7" // Температура в инкубаторе день 1
    const val DAYTEMP8 = "DAYTEMP8" // Температура в инкубаторе день 1
    const val DAYTEMP9 = "DAYTEMP9" // Температура в инкубаторе день 1
    const val DAYTEMP10 = "DAYTEMP10" // Температура в инкубаторе день 1
    const val DAYTEMP11 = "DAYTEMP11" // Температура в инкубаторе день 1
    const val DAYTEMP12 = "DAYTEMP12" // Температура в инкубаторе день 1
    const val DAYTEMP13 = "DAYTEMP13" // Температура в инкубаторе день 1
    const val DAYTEMP14 = "DAYTEMP14" // Температура в инкубаторе день 1
    const val DAYTEMP15 = "DAYTEMP15" // Температура в инкубаторе день 1
    const val DAYTEMP16 = "DAYTEMP16" // Температура в инкубаторе день 1
    const val DAYTEMP17 = "DAYTEMP17" // Температура в инкубаторе день 1
    const val DAYTEMP18 = "DAYTEMP18" // Температура в инкубаторе день 1
    const val DAYTEMP19 = "DAYTEMP19" // Температура в инкубаторе день 1
    const val DAYTEMP20 = "DAYTEMP20" // Температура в инкубаторе день 1
    const val DAYTEMP21 = "DAYTEMP21" // Температура в инкубаторе день 1
    const val DAYTEMP22 = "DAYTEMP22" // Температура в инкубаторе день 1
    const val DAYTEMP23 = "DAYTEMP23" // Температура в инкубаторе день 1
    const val DAYTEMP24 = "DAYTEMP24" // Температура в инкубаторе день 1
    const val DAYTEMP25 = "DAYTEMP25" // Температура в инкубаторе день 1
    const val DAYTEMP26 = "DAYTEMP26" // Температура в инкубаторе день 1
    const val DAYTEMP27 = "DAYTEMP27" // Температура в инкубаторе день 1
    const val DAYTEMP28 = "DAYTEMP28" // Температура в инкубаторе день 1
    const val DAYTEMP29 = "DAYTEMP29" // Температура в инкубаторе день 1
    const val DAYTEMP30 = "DAYTEMP30" // Температура в инкубаторе день 1

    //Влажность
    const val DAYDAMP1 = "DAYDAMP1" // Влажность в инкубаторе день 1
    const val DAYDAMP2 = "DAYDAMP2" // Влажность в инкубаторе день 1
    const val DAYDAMP3 = "DAYDAMP3" // Влажность в инкубаторе день 1
    const val DAYDAMP4 = "DAYDAMP4" // Влажность в инкубаторе день 1
    const val DAYDAMP5 = "DAYDAMP5" // Влажность в инкубаторе день 1
    const val DAYDAMP6 = "DAYDAMP6" // Влажность в инкубаторе день 1
    const val DAYDAMP7 = "DAYDAMP7" // Влажность в инкубаторе день 1
    const val DAYDAMP8 = "DAYDAMP8" // Влажность в инкубаторе день 1
    const val DAYDAMP9 = "DAYDAMP9" // Влажность в инкубаторе день 1
    const val DAYDAMP10 = "DAYDAMP10" // Влажность в инкубаторе день 1
    const val DAYDAMP11 = "DAYDAMP11" // Влажность в инкубаторе день 1
    const val DAYDAMP12 = "DAYDAMP12" // Влажность в инкубаторе день 1
    const val DAYDAMP13 = "DAYDAMP13" // Влажность в инкубаторе день 1
    const val DAYDAMP14 = "DAYDAMP14" // Влажность в инкубаторе день 1
    const val DAYDAMP15 = "DAYDAMP15" // Влажность в инкубаторе день 1
    const val DAYDAMP16 = "DAYDAMP16" // Влажность в инкубаторе день 1
    const val DAYDAMP17 = "DAYDAMP17" // Влажность в инкубаторе день 1
    const val DAYDAMP18 = "DAYDAMP18" // Влажность в инкубаторе день 1
    const val DAYDAMP19 = "DAYDAMP19" // Влажность в инкубаторе день 1
    const val DAYDAMP20 = "DAYDAMP20" // Влажность в инкубаторе день 1
    const val DAYDAMP21 = "DAYDAMP21" // Влажность в инкубаторе день 1
    const val DAYDAMP22 = "DAYDAMP22" // Влажность в инкубаторе день 1
    const val DAYDAMP23 = "DAYDAMP23" // Влажность в инкубаторе день 1
    const val DAYDAMP24 = "DAYDAMP24" // Влажность в инкубаторе день 1
    const val DAYDAMP25 = "DAYDAMP25" // Влажность в инкубаторе день 1
    const val DAYDAMP26 = "DAYDAMP26" // Влажность в инкубаторе день 1
    const val DAYDAMP27 = "DAYDAMP27" // Влажность в инкубаторе день 1
    const val DAYDAMP28 = "DAYDAMP28" // Влажность в инкубаторе день 1
    const val DAYDAMP29 = "DAYDAMP29" // Влажность в инкубаторе день 1
    const val DAYDAMP30 = "DAYDAMP30" // Влажность в инкубаторе день 1

    //Перевороты
    const val DAYOVERTURN1 = "DATOVERTURN1" // переворот в инкубаторе день 1
    const val DAYOVERTURN2 = "DATOVERTURN2" // переворот в инкубаторе день 1
    const val DAYOVERTURN3 = "DATOVERTURN3" // переворот в инкубаторе день 1
    const val DAYOVERTURN4 = "DATOVERTURN4" // переворот в инкубаторе день 1
    const val DAYOVERTURN5 = "DATOVERTURN5" // переворот в инкубаторе день 1
    const val DAYOVERTURN6 = "DATOVERTURN6" // переворот в инкубаторе день 1
    const val DAYOVERTURN7 = "DATOVERTURN7" // переворот в инкубаторе день 1
    const val DAYOVERTURN8 = "DATOVERTURN8" // переворот в инкубаторе день 1
    const val DAYOVERTURN9 = "DATOVERTURN9" // переворот в инкубаторе день 1
    const val DAYOVERTURN10 = "DATOVERTURN10" // переворот в инкубаторе день 1
    const val DAYOVERTURN11 = "DATOVERTURN11" // переворот в инкубаторе день 1
    const val DAYOVERTURN12 = "DATOVERTURN12" // переворот в инкубаторе день 1
    const val DAYOVERTURN13 = "DATOVERTURN13" // переворот в инкубаторе день 1
    const val DAYOVERTURN14 = "DATOVERTURN14" // переворот в инкубаторе день 1
    const val DAYOVERTURN15 = "DATOVERTURN15" // переворот в инкубаторе день 1
    const val DAYOVERTURN16 = "DATOVERTURN16" // переворот в инкубаторе день 1
    const val DAYOVERTURN17 = "DATOVERTURN17" // переворот в инкубаторе день 1
    const val DAYOVERTURN18 = "DATOVERTURN18" // переворот в инкубаторе день 1
    const val DAYOVERTURN19 = "DATOVERTURN19" // переворот в инкубаторе день 1
    const val DAYOVERTURN20 = "DATOVERTURN20" // переворот в инкубаторе день 1
    const val DAYOVERTURN21 = "DATOVERTURN21" // переворот в инкубаторе день 1
    const val DAYOVERTURN22 = "DATOVERTURN22" // переворот в инкубаторе день 1
    const val DAYOVERTURN23 = "DATOVERTURN23" // переворот в инкубаторе день 1
    const val DAYOVERTURN24 = "DATOVERTURN24" // переворот в инкубаторе день 1
    const val DAYOVERTURN25 = "DATOVERTURN25" // переворот в инкубаторе день 1
    const val DAYOVERTURN26 = "DATOVERTURN26" // переворот в инкубаторе день 1
    const val DAYOVERTURN27 = "DATOVERTURN27" // переворот в инкубаторе день 1
    const val DAYOVERTURN28 = "DATOVERTURN28" // переворот в инкубаторе день 1
    const val DAYOVERTURN29 = "DATOVERTURN29" // переворот в инкубаторе день 1
    const val DAYOVERTURN30 = "DATOVERTURN30" // переворот в инкубаторе день 1

    //Проветривание
    const val DAYAIRING1 = "DAYAIRING1" // Проветривание в инкубаторе
    const val DAYAIRING2 = "DAYAIRING2" // Проветривание в инкубаторе
    const val DAYAIRING3 = "DAYAIRING3" // Проветривание в инкубаторе
    const val DAYAIRING4 = "DAYAIRING4" // Проветривание в инкубаторе
    const val DAYAIRING5 = "DAYAIRING5" // Проветривание в инкубаторе
    const val DAYAIRING6 = "DAYAIRING6" // Проветривание в инкубаторе
    const val DAYAIRING7 = "DAYAIRING7" // Проветривание в инкубаторе
    const val DAYAIRING8 = "DAYAIRING8" // Проветривание в инкубаторе
    const val DAYAIRING9 = "DAYAIRING9" // Проветривание в инкубаторе
    const val DAYAIRING10 = "DAYAIRING10" // Проветривание в инкубаторе
    const val DAYAIRING11 = "DAYAIRING11" // Проветривание в инкубаторе
    const val DAYAIRING12 = "DAYAIRING12" // Проветривание в инкубаторе
    const val DAYAIRING13 = "DAYAIRING13" // Проветривание в инкубаторе
    const val DAYAIRING14 = "DAYAIRING14" // Проветривание в инкубаторе
    const val DAYAIRING15 = "DAYAIRING15" // Проветривание в инкубаторе
    const val DAYAIRING16 = "DAYAIRING16" // Проветривание в инкубаторе
    const val DAYAIRING17 = "DAYAIRING17" // Проветривание в инкубаторе
    const val DAYAIRING18 = "DAYAIRING18" // Проветривание в инкубаторе
    const val DAYAIRING19 = "DAYAIRING19" // Проветривание в инкубаторе
    const val DAYAIRING20 = "DAYAIRING20" // Проветривание в инкубаторе
    const val DAYAIRING21 = "DAYAIRING21" // Проветривание в инкубаторе
    const val DAYAIRING22 = "DAYAIRING22" // Проветривание в инкубаторе
    const val DAYAIRING23 = "DAYAIRING23" // Проветривание в инкубаторе
    const val DAYAIRING24 = "DAYAIRING24" // Проветривание в инкубаторе
    const val DAYAIRING25 = "DAYAIRING25" // Проветривание в инкубаторе
    const val DAYAIRING26 = "DAYAIRING26" // Проветривание в инкубаторе
    const val DAYAIRING27 = "DAYAIRING27" // Проветривание в инкубаторе
    const val DAYAIRING28 = "DAYAIRING28" // Проветривание в инкубаторе
    const val DAYAIRING29 = "DAYAIRING29" // Проветривание в инкубаторе
    const val DAYAIRING30 = "DAYAIRING30" // Проветривание в инкубаторе

    //Таблица по добавлению
    const val TABLE_STRUCTURE = ("CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY," + TITLE + " TEXT," +
            DISCROTION + " REAL," + DAY + " INTEGER," + MOUNT + " INTEGER," + YEAR + " INTEGER," + PRICEALL + " REAL)") // Структура таблицы 1

    //Таблица по продажам
    const val TABLE_STRUCTURESale = ("CREATE TABLE IF NOT EXISTS " +
            TABLE_NAMESALE + " (" + _ID + " INTEGER PRIMARY KEY," + TITLESale + " TEXT," +
            DISCROTIONSale + " REAL," + DAY + " INTEGER," + MOUNT + " INTEGER," + YEAR + " INTEGER," + PRICEALL + " REAL)") // Структура таблицы

    //Таблица по покупкам //TODO Возможно тут ошибка! REAL
    const val TABLE_STRUCTUREEXPENSES = ("CREATE TABLE IF NOT EXISTS " +
            TABLE_NAMEEXPENSES + " (" + _ID + " INTEGER PRIMARY KEY," + TITLEEXPENSES + " TEXT," +
            DISCROTIONEXPENSES + " REAL," + DAY + " INTEGER," + MOUNT + " INTEGER," + YEAR + " INTEGER," + COUNTEXPENSES + " REAL)") // Структура таблицы

    //Таблица по списанию
    const val TABLE_STRUCTUREWRITEOFF = ("CREATE TABLE IF NOT EXISTS " +
            TABLE_NAMEWRITEOFF + " (" + _ID + " INTEGER PRIMARY KEY," + TITLEWRITEOFF + " TEXT," +
            DISCROTIONSWRITEOFF + " REAL," + DAY + " INTEGER," + MOUNT + " INTEGER," + YEAR + " INTEGER," + STASTUSWRITEOFF + " INTEGER)") // Структура таблицы

    //Таблица по ценам
    const val TABLE_STRUCTUREPRICE = ("CREATE TABLE IF NOT EXISTS " +
            TABLE_NAMEPRICE + " (" + _ID + " INTEGER PRIMARY KEY," + TITLEPRISE + " TEXT," +
            DISCROTIONPRICE + " REAL)") // Структура таблицы

    //Таблица по товару
    const val TABLE_STRUCTUREPRODUCT = ("CREATE TABLE IF NOT EXISTS " +
            TABLE_NAMEPRODUCT + " (" + _ID + " INTEGER PRIMARY KEY," + TITLEPRODUCT + " TEXT," + STATUSPRODUCT + " TEXT)")

    //Таблица по инкубатору столбцов 13
    const val TABLE_STRUCTUREINCUBATOR = ("CREATE TABLE IF NOT EXISTS " +
            TABLE_INCUBATOR + " (" + _ID + " INTEGER PRIMARY KEY," + NAMEINCUBATOR + " TEXT," +
            TYPEINCUBATOR + " TEXT," + DATAINCUBATOR + " TEXT," + EGGALL + " TEXT," + EGGALLEND + " TEXT," + AIRING + " TEXT," + OVERTURNINCUBATOR + " TEXT," +
            ARHIVE + " TEXT," + DATAEND + " TEXT," + TIMEPUSH1 + " TEXT," + TIMEPUSH2 + " TEXT," + TIMEPUSH3 + " TEXT)")

    // Таблица по теплу и влажности инкубатора
    const val TABLE_STRUCTUREINCUBATORTEMPDAMP = ("CREATE TABLE IF NOT EXISTS " +
            TABLE_INCUBATORTEMPDAMP + " (" + _ID + " INTEGER PRIMARY KEY," + DAYTEMP1 + " TEXT," + DAYTEMP2 + " TEXT," + DAYTEMP3 + " TEXT," + DAYTEMP4 + " TEXT," + DAYTEMP5 + " TEXT," + DAYTEMP6 + " TEXT," + DAYTEMP7 + " TEXT," + DAYTEMP8 + " TEXT," + DAYTEMP9 + " TEXT," + DAYTEMP10 + " TEXT," + DAYTEMP11 + " TEXT," + DAYTEMP12 + " TEXT," + DAYTEMP13 + " TEXT," + DAYTEMP14 + " TEXT," + DAYTEMP15 + " TEXT," + DAYTEMP16 + " TEXT," + DAYTEMP17 + " TEXT," + DAYTEMP18 + " TEXT," + DAYTEMP19 + " TEXT," + DAYTEMP20 + " TEXT," + DAYTEMP21 + " TEXT," + DAYTEMP22 + " TEXT," + DAYTEMP23 + " TEXT," + DAYTEMP24 + " TEXT," + DAYTEMP25 + " TEXT," + DAYTEMP26 + " TEXT," + DAYTEMP27 + " TEXT," + DAYTEMP28 + " TEXT," + DAYTEMP29 + " TEXT," + DAYTEMP30 + " TEXT,"
            + DAYDAMP1 + " TEXT," + DAYDAMP2 + " TEXT," + DAYDAMP3 + " TEXT," + DAYDAMP4 + " TEXT," + DAYDAMP5 + " TEXT," + DAYDAMP6 + " TEXT," + DAYDAMP7 + " TEXT," + DAYDAMP8 + " TEXT," + DAYDAMP9 + " TEXT," + DAYDAMP10 + " TEXT," + DAYDAMP11 + " TEXT," + DAYDAMP12 + " TEXT," + DAYDAMP13 + " TEXT," + DAYDAMP14 + " TEXT," + DAYDAMP15 + " TEXT," + DAYDAMP16 + " TEXT," + DAYDAMP17 + " TEXT," + DAYDAMP18 + " TEXT," + DAYDAMP19 + " TEXT," + DAYDAMP20 + " TEXT," + DAYDAMP21 + " TEXT," + DAYDAMP22 + " TEXT," + DAYDAMP23 + " TEXT," + DAYDAMP24 + " TEXT," + DAYDAMP25 + " TEXT," + DAYDAMP26 + " TEXT," + DAYDAMP27 + " TEXT," + DAYDAMP28 + " TEXT," + DAYDAMP29 + " TEXT," + DAYDAMP30 + " TEXT)")

    // Таблица по теплу и влажности инкубатора
    const val TABLE_STRUCTUREINCUBATORTEMP = ("CREATE TABLE IF NOT EXISTS " +
            TABLE_INCUBATORTEMP + " (" + _ID + " INTEGER PRIMARY KEY," + DAYTEMP1 + " TEXT," + DAYTEMP2 + " TEXT," + DAYTEMP3 + " TEXT," + DAYTEMP4 + " TEXT," + DAYTEMP5 + " TEXT," + DAYTEMP6 + " TEXT," + DAYTEMP7 + " TEXT," + DAYTEMP8 + " TEXT," + DAYTEMP9 + " TEXT," + DAYTEMP10 + " TEXT," + DAYTEMP11 + " TEXT," + DAYTEMP12 + " TEXT," + DAYTEMP13 + " TEXT," + DAYTEMP14 + " TEXT," + DAYTEMP15 + " TEXT," + DAYTEMP16 + " TEXT," + DAYTEMP17 + " TEXT," + DAYTEMP18 + " TEXT," + DAYTEMP19 + " TEXT," + DAYTEMP20 + " TEXT," + DAYTEMP21 + " TEXT," + DAYTEMP22 + " TEXT," + DAYTEMP23 + " TEXT," + DAYTEMP24 + " TEXT," + DAYTEMP25 + " TEXT," + DAYTEMP26 + " TEXT," + DAYTEMP27 + " TEXT," + DAYTEMP28 + " TEXT," + DAYTEMP29 + " TEXT," + DAYTEMP30 + " TEXT)")

    // Таблица по теплу и влажности инкубатора
    const val TABLE_STRUCTUREINCUBATORDAMP = ("CREATE TABLE IF NOT EXISTS " +
            TABLE_INCUBATORDAMP + " (" + _ID + " INTEGER PRIMARY KEY," + DAYDAMP1 + " TEXT," + DAYDAMP2 + " TEXT," + DAYDAMP3 + " TEXT," + DAYDAMP4 + " TEXT," + DAYDAMP5 + " TEXT," + DAYDAMP6 + " TEXT," + DAYDAMP7 + " TEXT," + DAYDAMP8 + " TEXT," + DAYDAMP9 + " TEXT," + DAYDAMP10 + " TEXT," + DAYDAMP11 + " TEXT," + DAYDAMP12 + " TEXT," + DAYDAMP13 + " TEXT," + DAYDAMP14 + " TEXT," + DAYDAMP15 + " TEXT," + DAYDAMP16 + " TEXT," + DAYDAMP17 + " TEXT," + DAYDAMP18 + " TEXT," + DAYDAMP19 + " TEXT," + DAYDAMP20 + " TEXT," + DAYDAMP21 + " TEXT," + DAYDAMP22 + " TEXT," + DAYDAMP23 + " TEXT," + DAYDAMP24 + " TEXT," + DAYDAMP25 + " TEXT," + DAYDAMP26 + " TEXT," + DAYDAMP27 + " TEXT," + DAYDAMP28 + " TEXT," + DAYDAMP29 + " TEXT," + DAYDAMP30 + " TEXT)")

    // Таблица по перевороту инкубатора
    const val TABLE_STRUCTUREINCUBATOROVER = ("CREATE TABLE IF NOT EXISTS " +
            TABLE_INCUBATOROVER + " (" + _ID + " INTEGER PRIMARY KEY," + DAYOVERTURN1 + " TEXT," + DAYOVERTURN2 + " TEXT," + DAYOVERTURN3 + " TEXT," + DAYOVERTURN4 + " TEXT," + DAYOVERTURN5 + " TEXT," + DAYOVERTURN6 + " TEXT," + DAYOVERTURN7 + " TEXT," + DAYOVERTURN8 + " TEXT," + DAYOVERTURN9 + " TEXT," + DAYOVERTURN10 + " TEXT," + DAYOVERTURN11 + " TEXT," + DAYOVERTURN12 + " TEXT," + DAYOVERTURN13 + " TEXT," + DAYOVERTURN14 + " TEXT," + DAYOVERTURN15 + " TEXT," + DAYOVERTURN16 + " TEXT," + DAYOVERTURN17 + " TEXT," + DAYOVERTURN18 + " TEXT," + DAYOVERTURN19 + " TEXT," + DAYOVERTURN20 + " TEXT," + DAYOVERTURN21 + " TEXT," + DAYOVERTURN22 + " TEXT," + DAYOVERTURN23 + " TEXT," + DAYOVERTURN24 + " TEXT," + DAYOVERTURN25 + " TEXT," + DAYOVERTURN26 + " TEXT," + DAYOVERTURN27 + " TEXT," + DAYOVERTURN28 + " TEXT," + DAYOVERTURN29 + " TEXT," + DAYOVERTURN30 + " TEXT)")

    // Таблица по охлаждению инкубатора
    const val TABLE_STRUCTUREINCUBATORAIRING = ("CREATE TABLE IF NOT EXISTS " +
            TABLE_INCUBATORAIRING + " (" + _ID + " INTEGER PRIMARY KEY," + DAYAIRING1 + " TEXT," + DAYAIRING2 + " TEXT," + DAYAIRING3 + " TEXT," + DAYAIRING4 + " TEXT," + DAYAIRING5 + " TEXT," + DAYAIRING6 + " TEXT," + DAYAIRING7 + " TEXT," + DAYAIRING8 + " TEXT," + DAYAIRING9 + " TEXT," + DAYAIRING10 + " TEXT," + DAYAIRING11 + " TEXT," + DAYAIRING12 + " TEXT," + DAYAIRING13 + " TEXT," + DAYAIRING14 + " TEXT," + DAYAIRING15 + " TEXT," + DAYAIRING16 + " TEXT," + DAYAIRING17 + " TEXT," + DAYAIRING18 + " TEXT," + DAYAIRING19 + " TEXT," + DAYAIRING20 + " TEXT," + DAYAIRING21 + " TEXT," + DAYAIRING22 + " TEXT," + DAYAIRING23 + " TEXT," + DAYAIRING24 + " TEXT," + DAYAIRING25 + " TEXT," + DAYAIRING26 + " TEXT," + DAYAIRING27 + " TEXT," + DAYAIRING28 + " TEXT," + DAYAIRING29 + " TEXT," + DAYAIRING30 + " TEXT)")

    val DROP_TABLESale1: String =
        "DROP TABLE IF EXISTS" + TABLE_NAMESALE // сброс продаж
    val DROP_TABLEEXPENSES: String =
        "DROP TABLE IF EXISTS" + TABLE_NAMEEXPENSES // сброс покупок
    val DROP_TABLE: String = "DROP TABLE IF EXISTS" + TABLE_NAME // сброс обычной
    val DROP_TABLEPRICE: String = "DROP TABLE IF EXISTS" + TABLE_NAMEPRICE // сброс цен
    val DROP_TABLEWRITEOFF: String =
        "DROP TABLE IF EXISTS" + TABLE_NAMEWRITEOFF // сброс цен
    val DROP_TABLEPRODUCT: String =
        "DROP TABLE IF EXISTS" + TABLE_NAMEPRODUCT // сброс продуктов
    val DROP_TABLEINCUBATOR: String =
        "DROP TABLE IF EXISTS" + TABLE_INCUBATOR // сброс Инкубатора
    val DROP_TABLEINCUBATORTEMPDAMP: String =
        "DROP TABLE IF EXISTS " + TABLE_INCUBATORTEMPDAMP // сброс Инкубатора
    val DROP_TABLEINCUBATOROVER: String =
        "DROP TABLE IF EXISTS" + TABLE_INCUBATOROVER // сброс Инкубатора
    val DROP_TABLEINCUBATORAIRING: String =
        "DROP TABLE IF EXISTS" + TABLE_INCUBATORAIRING // сброс Инкубатора
}