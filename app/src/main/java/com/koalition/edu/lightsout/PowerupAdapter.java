package com.koalition.edu.lightsout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by G301 on 1/26/2016.
 */
public class PowerupAdapter
        extends RecyclerView.Adapter<PowerupAdapter.PowerupHolder>{

    private final Context context;
    ArrayList<PowerUp> powerups;
    Typeface pixelFont;

    DatabaseHelper dbHelper;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public PowerupAdapter(ArrayList<PowerUp> songs, Context context){
        this.powerups = songs;
        this.context = context;

        dbHelper = new DatabaseHelper(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public class PowerupHolder extends RecyclerView.ViewHolder{
        TextView tvPowerupTitle;
        ImageView ivPowerupIcon;
        TextView tvPowerupCost;
        View container;
        public PowerupHolder(View itemView) {
            super(itemView);
            pixelFont = Typeface.createFromAsset(context.getAssets(),"fonts/pixelmix.ttf");
            tvPowerupTitle = (TextView) itemView.findViewById(R.id.powerup_title);
            tvPowerupCost = (TextView) itemView.findViewById(R.id.powerup_cost);
            ivPowerupIcon = (ImageView) itemView.findViewById(R.id.powerup_icon);
            container = itemView.findViewById(R.id.container);

            tvPowerupTitle.setTypeface(pixelFont);
            tvPowerupCost.setTypeface(pixelFont);
        }
    }

    @Override
    public PowerupHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_powerup, viewGroup, false);

        return new PowerupHolder(v);
    }

    @Override
    public void onBindViewHolder(PowerupHolder powerupHolder, int position) {
        // TODO: change textView's text to current song
        System.out.println("WAT IS LYF 1");
        PowerUp powerUp = powerups.get(position);

        int idResource = context.getResources().getIdentifier(powerUp.getIconTite(), "id", PowerUpListActivity.class.getPackage().getName());
        System.out.println("WAT IS LYF 2");

        powerupHolder.tvPowerupTitle.setText(powerUp.getTitle());
        powerupHolder.tvPowerupCost.setText(String.valueOf(powerUp.getPrice()));
        powerupHolder.ivPowerupIcon.setImageResource(idResource);

        powerupHolder.container.setTag(R.id.key_item_holder, powerupHolder);
        powerupHolder.container.setTag(R.id.key_item_powerup, powerUp);

        System.out.println("WAT IS LYF 3");
//
//        powerupHolder.container.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                int position = ((SongHolder)v.getTag(R.id.key_item_holder)).getAdapterPosition();
//                songs.remove(position);
//                notifyItemRemoved(position);
//                return false;
//            }
//        });
//
        //WORKS BUT DELAY FAKER
        powerupHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("ANO DAW WTF");
//
                PowerUp powerUp = (PowerUp) v.getTag(R.id.key_item_powerup);

                //IGNORE Muna
                Intent intent=new Intent(context, ShopDialogActivity.class);
                intent.putExtra("shopItemID", powerUp.getId());
                context.startActivity(intent);
            }
        });

        System.out.println("WAT IS LYF 4");
    }

    @Override
    public int getItemCount() {
        return this.powerups.size();
    }


}
