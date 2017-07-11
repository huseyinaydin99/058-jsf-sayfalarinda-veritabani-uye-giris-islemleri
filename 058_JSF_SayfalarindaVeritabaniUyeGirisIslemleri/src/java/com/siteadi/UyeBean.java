/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siteadi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author husey
 */
@ManagedBean(name = "ub")
@SessionScoped
public class UyeBean {

    /**
     * Creates a new instance of UyeBean
     */
    private int uyeId;
    private String userName, password, adiSoyadi,durum = "kapali",onemliBilgi;

    public UyeBean() {
    }

    public int getUyeId() {
        return uyeId;
    }

    public void setUyeId(int uyeId) {
        this.uyeId = uyeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdiSoyadi() {
        return adiSoyadi;
    }

    public void setAdiSoyadi(String adiSoyadi) {
        this.adiSoyadi = adiSoyadi;
    }

    private String dbuyeusername;

    public String getDbuyeusername() {
        return dbuyeusername;
    }

    private String dbuyepassword;

    public String getDbuyepassword() {
        return dbuyepassword;
    }

    public String getDurum() {
        return durum;
    }
    
    public String test(){
        //baglandı
        onemliBilgi = "yarak";
        return durum;
    }

    public String getOnemliBilgi() {
        return onemliBilgi;
    }
    
    
    

    private Connection connection = null;
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;

    public void bilgileriOnayla(String gelenUserName, String gelenPassword) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/firmaveritabani", "root", "toor");
            String kullaniciGirisSorgusu = "select * from uye where USERNAME = ? and PASSWORD = ?";
            preparedStatement = connection.prepareStatement(kullaniciGirisSorgusu);
            
            preparedStatement.setString(1, gelenUserName);
            preparedStatement.setString(2, gelenPassword);
            
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) System.out.println("Giriş Onaylandı");
            else System.out.println("Giriş Başarısız");
            
            dbuyeusername = resultSet.getString(2);
            dbuyepassword = resultSet.getString(3);
            
            uyeId = resultSet.getInt(1);
            userName = resultSet.getString(2);
            password = resultSet.getString(3);
            adiSoyadi = resultSet.getString(4);
            
        } catch (Exception ex) {
            System.err.println("hata var.!");
        } finally {
            System.out.println("Sağol");
        }
    }
    
    public String uyeGirisiniDenetle(){
        System.err.println("uyeGirisiDenetle metodu çalıştı");
        bilgileriOnayla(userName, password);
        if(userName.equalsIgnoreCase(dbuyeusername) && password.equalsIgnoreCase(dbuyepassword)){
            System.err.println("uyeGirisiDenetle metodu if çalıştı");
            durum = "acik";
            return "gecerli";
        }
        else{
            durum = "kapali";
            return "gecersiz";
        }
    }
    
    public String uyeCikisiniDenetle(){
        /*FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        final HttpServletRequest httpServletRequest = (HttpServletRequest) externalContext.getRequest();
        httpServletRequest.getSession(false).invalidate();/* 
        
        bu kodlar sessionu bulur ve sessionu kapatır. yani kullanıcıya ait oturum kapanır
        */
        
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        onemliBilgi = "";
        return "uyecikisi?faces-redirect=true";
    }
}
