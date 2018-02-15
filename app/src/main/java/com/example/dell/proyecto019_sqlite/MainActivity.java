package com.example.dell.proyecto019_sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText et1, et2, et3;
    private TextView txLista;
    private ImageButton btn;
    Cursor puntero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        et3=(EditText)findViewById(R.id.et3);
        txLista=(TextView)findViewById(R.id.txtLista);


    }

    public void alta (View v){

        //ADMINISTRADOR ES EL NOMBRE DE LA BASE DE DATOS
        //SI NO EXISTE, AL CREAR OBJETO DE LA CLASE ADMIN SE CREA LA BASE DE DATOS
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administrador", null, 1);

        //CON ESTE OBJETO ESCRIBIMOS EN LA BASE DE DATOS
        SQLiteDatabase bd=admin.getWritableDatabase();

        String miCodigo=et1.getText().toString();
        String miDescripcion=et2.getText().toString();
        String miPrecio=et3.getText().toString();

        //A TRAVES DE ESTE OBJETO ContentVales  PREPARAMOS LOS REGISTROS POR CLAVE VALOR
        ContentValues registro=new ContentValues();
        registro.put("codigo", miCodigo);
        registro.put("descripcion", miDescripcion);
        registro.put("precio", miPrecio);

        //INSERTAMOS EL REGISTRO
        try {
            //
            bd.insert("articulos", null, registro);
        }catch (Exception e){
            Toast.makeText(this, "No se cargó el registro en la BD", Toast.LENGTH_LONG).show();
        }

        bd.close();

        et1.setText("");
        et2.setText("");
        et3.setText("");

        Toast.makeText(this, "Se cargó en registro en la BD", Toast.LENGTH_LONG).show();
    }

    public void consultaPorCodigo(View v){

        //SE CREA INSTANCIA DE LA BASE DE DATOS
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administrador", null, 1);

        //SE RECUPERA LA BASE DE DATOS
        SQLiteDatabase bd=admin.getWritableDatabase();
        String miCodigo=et1.getText().toString();

        Cursor fila=bd.rawQuery("select descripcion, precio from articulos where codigo=" + miCodigo, null);

        if(fila.moveToFirst()){
            et2.setText(fila.getString(0)); //El 0 representará el primero de los campos recuperados en la consulta(descripcion)
            et3.setText(fila.getString(1)); //El 1 representará el segundo de los campos recuperados en la consulta(precio)
        }
        else
            Toast.makeText(this, "No existe artículo con ese código", Toast.LENGTH_LONG).show();
        bd.close();
    }

    public void consultaPorDescripcion(View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administrador", null, 1);
        SQLiteDatabase bd=admin.getWritableDatabase();
        String miDescripcion=et2.getText().toString();
        Cursor fila=bd.rawQuery("select codigo, precio from articulos where descripcion='" + miDescripcion + "'", null);
        if(fila.moveToFirst()){
            et1.setText(fila.getString(0));
            et3.setText(fila.getString(1));
        }
        else
            Toast.makeText(this, "No existe articulo con esa descripción", Toast.LENGTH_LONG).show();
        bd.close();
    }

    public void bajaPorCodigo(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administrador", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String miCodigo= et1.getText().toString();
        int cantidadElementosBorrados = bd.delete("articulos", "codigo=" + miCodigo, null);
        bd.close();
        et1.setText("");
        et2.setText("");
        et3.setText("");
        if (cantidadElementosBorrados == 1)
            Toast.makeText(this, "Se borró el artículo con dicho código", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "No existe un artículo con dicho código", Toast.LENGTH_SHORT).show();
    }

    public void modificacion(View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administrador", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String miCodigo = et1.getText().toString();
        String miDescripcion = et2.getText().toString();
        String miPrecio = et3.getText().toString();
        ContentValues registro = new ContentValues();
        registro.put("codigo", miCodigo);
        registro.put("descripcion", miDescripcion);
        registro.put("precio", miPrecio);
        int cantidadElementosActualizados = bd.update("articulos", registro, "codigo=" + miCodigo, null);
        bd.close();
        if (cantidadElementosActualizados == 1)
            Toast.makeText(this, "se modificaron los datos", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "no existe un artículo con el código ingresado", Toast.LENGTH_SHORT).show();
    }

    //EJERCICIOS INDICADOS POR MANOLO.

    public void listar(View v){
        String lista="";
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administrador", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila=bd.rawQuery("select * from articulos", null);
        //fila.moveToFirst();
        while(fila.moveToNext()){
            lista+=fila.getString(0)+" "+fila.getString(1)+" "+fila.getString(2)+"\n";
        }
        txLista.setText(lista);
    }
    public void mover(View v){
        //recupero Imagen Button seleccionado, puede ser cualquiera de los dos
    btn=(ImageButton) v;

        //tengo un Objeto Cursor llamado puntero como variable global
        //si nunca ha sido pulsado, será nulo
        //si es nulo hago la primera consulta
        if(puntero==null){
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administrador", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();
            puntero=bd.rawQuery("select * from articulos", null);
            puntero.moveToFirst();
            et1.setText(puntero.getString(0));
            et2.setText(puntero.getString(1));
            et3.setText(puntero.getString(2));

        }
        else{

            //Ya ha sido pulsado alguna vez, ek objeto Puntero siempre guadará un registro
            //bt1=atras
            if (btn.getId()==R.id.ib1){
                //si estamos en el primer registro, nos saltará un Mensaje indicando que estamos en el ultimo registro
                if(puntero.isFirst()) {
                    Toast.makeText(this, "Esta usted en el primer registro", Toast.LENGTH_SHORT).show();

                    /*
                        PARA QUE PASE AL ÙLTIMO SERIA ASÍ

                         puntero.moveToLast();
                          et1.setText(puntero.getString(0));
                          et2.setText(puntero.getString(1));
                          et3.setText(puntero.getString(2));
                    *
                    * */

                }else {
                    puntero.moveToPrevious();
                    et1.setText(puntero.getString(0));
                    et2.setText(puntero.getString(1));
                    et3.setText(puntero.getString(2));
                }
                //bt2=delante
            }else{
                //SI ES EL ULTIMO SALDRÁ TOAST INDICANDO QUE ESTAMOS EN EL ULTIMO REGISTRO
                if(puntero.isLast()) {
                    Toast.makeText(this, "Esta usted en el último registro registro", Toast.LENGTH_SHORT).show();
                    /*
                        PARA QUE PASE AL PRIMERO

                          puntero.moveToFirst();
                          et1.setText(puntero.getString(0));
                          et2.setText(puntero.getString(1));
                          et3.setText(puntero.getString(2));
                    *
                    * */
                }else {
                    puntero.moveToNext();
                    et1.setText(puntero.getString(0));
                    et2.setText(puntero.getString(1));
                    et3.setText(puntero.getString(2));
                }

            }
        }

    }


}
