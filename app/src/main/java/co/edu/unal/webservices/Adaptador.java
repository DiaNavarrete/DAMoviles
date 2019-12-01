package co.edu.unal.webservices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adaptador extends BaseAdapter {
    private List<Item> listItems;
    private Context context;

    public Adaptador(List<Item> listItems, Context context) {
        this.context = context;
        if (listItems ==null) this.listItems =new ArrayList<Item>();
        else this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return listItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Item item=(Item)getItem(i);
        view= LayoutInflater.from(context).inflate(R.layout.item_view,null);
        TextView tit=view.findViewById(R.id.item_title);
        TextView cont=view.findViewById(R.id.item_content);
        TextView val=view.findViewById(R.id.item_val);
        tit.setText(item.getOperador());
        cont.setText(item.getKpi());
        val.setText(item.getValor());
        return view;
    }
}
