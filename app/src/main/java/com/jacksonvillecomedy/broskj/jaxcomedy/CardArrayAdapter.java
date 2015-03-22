package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 3/22/2015.
 */
public class CardArrayAdapter extends ArrayAdapter<Card> {
    private List<Card> cardList = new ArrayList<Card>();

    static class CardViewHolder {
        TextView tvDate;
        TextView tvInfo;
    }

    public CardArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(Card card) {
        cardList.add(card);
        super.add(card);
    }

    @Override
    public Card getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.tvDate = (TextView) row.findViewById(R.id.tvUpcomingShowsDate);
            viewHolder.tvInfo = (TextView) row.findViewById(R.id.tvUpcomingShowsInfo);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }
        Card card = getItem(position);
        viewHolder.tvDate.setText(card.getShowsDate());
        viewHolder.tvInfo.setText(card.getShowsInfo());
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}//end class CardArrayAdapter
