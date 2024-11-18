module labzerothree {
    exports pl.pwr.ite.dynak.main;
    exports pl.pwr.ite.dynak.tenant;
    exports pl.pwr.ite.dynak.landlord;
    exports pl.pwr.ite.dynak.controller;
    requires static lombok;
    requires javafx.controls;
    requires javafx.graphics;
    requires java.sql;
    requires org.xerial.sqlitejdbc; //db
    //requires org.slf4j; //logger
}