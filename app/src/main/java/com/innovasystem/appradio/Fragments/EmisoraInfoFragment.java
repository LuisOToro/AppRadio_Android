package com.innovasystem.appradio.Fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.innovasystem.appradio.Activities.HomeActivity;
import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.Classes.Models.RedSocialEmisora;
import com.innovasystem.appradio.Classes.Models.TelefonoEmisora;
import com.innovasystem.appradio.Classes.RestServices;
import com.innovasystem.appradio.R;

import com.squareup.picasso.Picasso;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmisoraInfoFragment extends Fragment {


    JustifiedTextView tv_descripcion_emisora;
    TextView tv_web_emisora,tv_telef_emisora,tv_ubicacion_emisora;
    LinearLayout layoutManager,redesSociales;
    ImageView logo;
    Emisora emisora;


    public EmisoraInfoFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_emisora_info, container, false);
        tv_descripcion_emisora= root.findViewById(R.id.tv_descripcion_emisora);
        logo=root.findViewById(R.id.logo_radio);
        tv_web_emisora= root.findViewById(R.id.tv_web_emisora);
        tv_telef_emisora= root.findViewById(R.id.tv_telef_emisora);
        tv_ubicacion_emisora= root.findViewById(R.id.tv_ubicacion_emisora);
        layoutManager= root.findViewById(R.id.lin_layout_info_emisora);
        redesSociales=root.findViewById(R.id.tv_redesSociales);

        emisora= EmisoraContentFragment.emisora;

        tv_descripcion_emisora.setText(emisora.getDescripcion());
        tv_web_emisora.setText(emisora.getSitio_web().substring(7));
        //tv_telef_emisora.setText("1234567890 - 11122233345");
        tv_ubicacion_emisora.setText(emisora.getDireccion());

        new RestFetchEmisoraInfoTask().execute();

        return root;
    }


    /**
     * Clase Task que obtiene los datos de telefono y redes sociales de la emisora
     */
    private class RestFetchEmisoraInfoTask extends AsyncTask<Void,Void,Void>{
        List<TelefonoEmisora> telefonos;
        List<RedSocialEmisora> redes;

        @Override
        protected Void doInBackground(Void... voids) {
            telefonos= RestServices.consultarTelefonosEmisora(getContext(),emisora.getId().intValue());
            redes= RestServices.consultarRedesEmisora(getContext(),emisora.getId().intValue());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(telefonos!= null){
                String telfs="";
                for (TelefonoEmisora tel: this.telefonos) {
                    telfs+= tel.getNro_telefono() + " - ";
                }
                telfs= telfs.substring(0,telfs.length()-2);
                tv_telef_emisora.setText(telfs);
            }
            else{
                tv_telef_emisora.setText("Ninguno");
            }
            if(emisora.getNombre().equals("Radio Diblu")){
                logo.setBackground(getContext().getDrawable(R.drawable.logo_diblu));
            }
            if(emisora.getNombre().equals("Radio Caravana")){
                logo.setBackground(getContext().getDrawable(R.drawable.logo_caravana));
            }
            if(redes!= null){
                LinearLayout container;
                Button button;
                ImageView imgView;

                for(RedSocialEmisora red: redes){
                    if(red.getNombre().equalsIgnoreCase("Facebook")){
                        imgView= new ImageView(getContext());
                        Picasso.with(getContext()).load(R.drawable.facebook_info).resize(30,35).into(imgView);

                    }
                    else if(red.getNombre().equalsIgnoreCase("Twitter")){
                        imgView= new ImageView(getContext());
                        Picasso.with(getContext()).load(R.drawable.twiter_info).resize(30,35).into(imgView);
                    }
                    else if(red.getNombre().equalsIgnoreCase("Youtube")){
                        imgView= new ImageView(getContext());
                        Picasso.with(getContext()).load(R.drawable.youtube_icon).resize(25,35).into(imgView);
                    }
                    else{
                        imgView= new ImageView(getContext());
                        Picasso.with(getContext()).load(R.drawable.social_media_icon).centerInside().into(imgView);
                    }
                    LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,0.05f);
                    layoutParams.setMarginStart(10);
                    layoutParams.setMarginEnd(10);
                    layoutParams.gravity= Gravity.CENTER;
                    imgView.setLayoutParams(layoutParams);

                    SpannableString username;
                    ///(?:http://)?(?:www.)?facebook.com/(?:(?:w)*#!/)?(?:pages/)?(?:[w-]*/)*([w-]*)/

                    if(red.getLink().matches("(.*)\\.com/(\\w|\\s|\\d)+")|| red.getLink().matches("(.*)\\.com/(\\w|\\s)+[0-9][0-9]\\.[0-9]/")|| red.getLink().matches("(.*)\\.com/(\\w|\\s)+/")){
                        int index= red.getLink().indexOf("com/");
                        if(red.getNombre().equals("Facebook")){
                            username= new SpannableString(red.getLink().substring(index+4,red.getLink().length()-1 ) );
                        }else{
                            username= new SpannableString(red.getLink().substring(index+4,red.getLink().length() ) );
                        }
                    }
                    else{
                        username= new SpannableString(red.getLink());
                    }

                    username.setSpan(new UnderlineSpan(), 0,username.length(),0);
                    button=new Button(getContext());
                    button.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
                    button.setText(username);
                    button.setTextSize(12);
                    button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    button.setAllCaps(false);
                    button.setTextColor(getContext().getResources().getColor(R.color.color_text));
                    button.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.45f));
                    button.setOnClickListener((View v)->{
                        WebFragment fragment= new WebFragment();
                        Bundle args= new Bundle();
                        args.putString("url",red.getLink());
                        fragment.setArguments(args);
                        ((HomeActivity) getContext()).changeFragment(fragment,R.id.frame_container,true);
                    });

                    container= new LinearLayout(getContext());
                    container.addView(imgView);
                    container.addView(button);

                    redesSociales.addView(container);


                }
            }
        }
    }

}
