package com.koalition.edu.lightsout;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by G301 on 1/26/2016.
 */
public class PowerupAdapter
        extends RecyclerView.Adapter<PowerupAdapter.PowerupHolder>{

    ArrayList<PowerUp> powerups;

    public PowerupAdapter(ArrayList<PowerUp> songs){
        this.powerups = songs;
    }

    public class PowerupHolder extends RecyclerView.ViewHolder{
        TextView tvPowerupTitle;
        ImageView ivPowerupIcon;
        TextView tvPowerupCost;
        View container;
        public PowerupHolder(View itemView) {
            super(itemView);
            tvPowerupTitle = (TextView) itemView.findViewById(R.id.powerup_title);
            tvPowerupCost = (TextView) itemView.findViewById(R.id.powerup_cost);
            container = itemView.findViewById(R.id.container);
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
        PowerUp powerUp = powerups.get(position);
        powerupHolder.tvPowerupTitle.setText(powerUp.getTitle());
        powerupHolder.tvPowerupCost.setText(String.valueOf(powerUp.getPrice()));
//        powerupHolder.container.setTag(R.id.key_item_holder, powerupHolder);
//        powerupHolder.container.setTag(R.id.key_item_song, song);
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
//        powerupHolder.container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Song s = (Song) v.getTag(R.id.key_item_song);
//                v.getContext().startActivity(new Intent(v.getContext(),ViewSongActivity.class)
//                        .putExtra(MainActivity.KEY_TITLE, s.getTitle())
//                        .putExtra(MainActivity.KEY_LYRICS, s.getLyrics()));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return this.powerups.size();
    }

//    public void addSong(Song song){
//        songs.add(song);
//        notifyItemInserted(getItemCount()-1);
//    }

}