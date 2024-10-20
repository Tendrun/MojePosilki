package Project.mojeposilki;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private Context context;
    private List<String> ingredients;

    public IngredientAdapter(Context context, List<String> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the ingredient list item layout
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_list_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        // Set the ingredient data to the EditText
        String ingredient = ingredients.get(position);
        holder.ingredientEditText.setText(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    // ViewHolder class to hold the EditText
    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        EditText ingredientEditText;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientEditText = itemView.findViewById(R.id.ingredientEditText);
        }
    }
}
