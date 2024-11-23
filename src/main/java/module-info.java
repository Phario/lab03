module labzerothree {
    exports pl.pwr.ite.dynak.main;
    exports pl.pwr.ite.dynak.tenant;
    exports pl.pwr.ite.dynak.landlord;
    exports pl.pwr.ite.dynak.controller;
    exports pl.pwr.ite.dynak.dataUtils;
    requires static lombok;
    requires javafx.controls;
    requires javafx.graphics;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.rmi;
    requires java.desktop;
    requires jdk.xml.dom;
    requires org.slf4j; //db
    //requires org.slf4j; //logger
}