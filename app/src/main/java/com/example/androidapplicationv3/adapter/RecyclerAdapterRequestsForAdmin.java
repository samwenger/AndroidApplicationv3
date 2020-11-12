package com.example.androidapplicationv3.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapplicationv3.R;
import com.example.androidapplicationv3.database.pojo.RequestWithUser;
import com.example.androidapplicationv3.util.RecyclerViewItemClickListener;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class RecyclerAdapterRequestsForAdmin<T> extends RecyclerView.Adapter<RecyclerAdapterRequestsForAdmin.ViewHolder>{

    private List<T> mData;
    private RecyclerViewItemClickListener mListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mTextView;
        ViewHolder(TextView textView) {
            super(textView);
            mTextView = textView;
        }
    }

    public RecyclerAdapterRequestsForAdmin(RecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerAdapterRequestsForAdmin.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(view -> mListener.onItemClick(view, viewHolder.getAdapterPosition()));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterRequestsForAdmin.ViewHolder holder, int position) {
        T item = mData.get(position);
        if (item.getClass().equals(RequestWithUser.class)){
            RequestWithUser requestItem = (RequestWithUser) item;

            String dateDebut = new SimpleDateFormat("dd/MM/yyyy").format(requestItem.request.getDateDebut());
            String dateFin = new SimpleDateFormat("dd/MM/yyyy").format(requestItem.request.getDateFin());

            holder.mTextView.setText(requestItem.user.getLastname() + " " + requestItem.user.getFirstname() + " ("
                    + dateDebut + " - " + dateFin + ") (" +
                    requestItem.type.getType() + ")");

        }
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    public void setData(final List<T> data) {
        if (mData == null) {
            mData = data;
            notifyItemRangeInserted(0, data.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mData.size();
                }

                @Override
                public int getNewListSize() {
                    return data.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mData instanceof RequestWithUser) {
                        return ((RequestWithUser) mData.get(oldItemPosition)).request.getIdRequest().equals(((RequestWithUser) data.get(newItemPosition)).request.getIdRequest());
                    }

                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mData instanceof RequestWithUser) {
                        RequestWithUser newRequest = (RequestWithUser) data.get(newItemPosition);
                        RequestWithUser oldRequest = (RequestWithUser) mData.get(newItemPosition);
                        return newRequest.request.getIdRequest().equals(oldRequest.request.getIdRequest())
                                && Objects.equals(newRequest.request.getDateDebut(), oldRequest.request.getDateDebut())
                                && Objects.equals(newRequest.request.getDateFin(), oldRequest.request.getDateFin())
                                && Objects.equals(newRequest.request.getIdType(), oldRequest.request.getIdType());
                    }
                    return false;
                }
            });
            mData = data;
            result.dispatchUpdatesTo(this);
        }
    }
}
