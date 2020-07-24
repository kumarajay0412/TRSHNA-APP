package com.example.tishna;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest{

    private static final String  REGISTER_REQUEST_URL="https://tishna.000webhostapp.com/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String name, int age, String username,String password,int daily_limit, Response.Listener<String> listener){
        super(Method.POST,REGISTER_REQUEST_URL,listener,null);

        params =new HashMap<>();
        params.put("name",name);
        params.put("age",age+"");
        params.put("username",username);
        params.put("password",password);
        params.put("daily_limit",daily_limit+"");
    }

    @Override
    public Map<String, String> getParams() {

        return params;
    }
}

