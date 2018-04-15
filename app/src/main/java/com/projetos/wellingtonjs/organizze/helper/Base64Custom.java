package com.projetos.wellingtonjs.organizze.helper;

import android.util.Base64;

/**
 * Created by wellington JS on 31/03/2018.
 */

public class Base64Custom {

    public static String codoficarBase64(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)","");

    }

    public static String decodoficarBase64(String textoCodificado){
         return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}
