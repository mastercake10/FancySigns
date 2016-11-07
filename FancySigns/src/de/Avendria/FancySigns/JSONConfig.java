package de.Avendria.FancySigns;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;

import com.google.gson.Gson;


public class JSONConfig {
	File data;
	Type type;
	
	public JSONConfig(String path, Class<?> type){
		this.data = new File(path);
		this.type = type;
	}
	public void saveToDisk(Object d){
		Gson gson = new Gson();
		try {
			FileOutputStream fout= new FileOutputStream (data);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			
			oos.writeObject(gson.toJson(d, type));
			fout.close();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Object get(){
		Object  c = null;
		if(data.exists()){
			try {
				FileInputStream fin = new FileInputStream(data);
				ObjectInputStream ois = new ObjectInputStream(fin);
				Gson gson = new Gson();
				c = gson.fromJson((String) ois.readObject(), type);
				ois.close();
				fin.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return c;
		}else{
			return null;
		}
	}
}
