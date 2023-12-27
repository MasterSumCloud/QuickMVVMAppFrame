package com.demo.appframe.net;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class StringAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
        out.value(value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return "";
        }

        // number类的也会执行这个方法，但是number返回null 会报错，所以把Number排除
        if (in.peek() != JsonToken.STRING && in.peek() != JsonToken.NUMBER) {
            // LogUtils.e("typeAdapter","stringTypeAdapter 不是String = "+in.peek());
            //不是String，直接跳过，不解析
            in.skipValue();
            return null;
        }
        String result = in.nextString();
        return result;
    }
}
