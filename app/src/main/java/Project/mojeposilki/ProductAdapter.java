package Project.mojeposilki;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public ProductAdapter(List<Product> productList, OnDeleteClickListener onDeleteClickListener) {
        this.productList = productList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.skladnikField.setText(product.getSkladnik());
        holder.iloscField.setText(product.getIlosc());
        holder.jednostkaField.setText(product.getJednostka());
        holder.KategoriaField.setText(product.getKategoria());

        // Add TextWatchers to update Product object when the user types
        holder.skladnikField.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                product.setSkladnik(s.toString());
            }
        });

        holder.iloscField.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                product.setIlosc(s.toString());
            }
        });

        holder.jednostkaField.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                product.setJednostka(s.toString());
            }
        });

        holder.KategoriaField.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                product.setKategoria(s.toString());
            }
        });

        holder.deleteProductButton.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        EditText skladnikField, iloscField, jednostkaField, KategoriaField;
        Button deleteProductButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            skladnikField = itemView.findViewById(R.id.skladnikField);
            iloscField = itemView.findViewById(R.id.iloscField);
            jednostkaField = itemView.findViewById(R.id.jednostkaField);
            KategoriaField = itemView.findViewById(R.id.KategoriaField);
            deleteProductButton = itemView.findViewById(R.id.deleteProductButton);
        }
    }
}


