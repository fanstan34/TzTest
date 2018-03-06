package com.example.lichang.casualchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lichang.casualchat.utils.CommonUtils;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

public class ChatActivity extends Activity {

    private ListView listView;
    private int chatType = 1;
    private String toChatUsername;
    private Button btn_send;
    private EditText et_content;
    private List<EMMessage> msgList;
    MessageAdapter adapter;
    private EMConversation conversation;
    protected  int pagesize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //获取用户名并显示到面部
        toChatUsername = this.getIntent().getStringExtra("username");
        TextView tv_toUsername = (TextView) this.findViewById(R.id.contact_username);
        tv_toUsername.setText(toChatUsername);
        listView = (ListView) this.findViewById(R.id.listView);
        btn_send = (Button) this.findViewById(R.id.btn_send);
        et_content = (EditText) this.findViewById(R.id.et_content);
        getAllMessage();
        msgList = conversation.getAllMessages();
        adapter = new MessageAdapter(msgList, ChatActivity.this);
        listView.setAdapter(adapter);
        listView.setSelection(listView.getCount() - 1);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_content.getText().toString().trim();
                if(TextUtils.isEmpty(content)) {
                    return;
                }
                setMesaage(content);
            }
        });
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    protected  void getAllMessage(){
        //获取当前conversation对象
        conversation =EMClient.getInstance().chatManager().getConversation(toChatUsername,
                CommonUtils.getConversationType(chatType), true);
        //把此会话未读数置为0
        conversation.markAllMessagesAsRead();
        // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize){
            String msgId = null;
            if(msgs != null && msgs.size() > 0){
                msgId = msgs.get(0).getMsgId();
            }
            conversation.loadMoreMsgFromDB(msgId,pagesize - msgCount);
        }
    }

    private void setMesaage(String content) {

        // 创建一条文本消息，content为消息文字内容，toChatUsername为对方用户
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
       adapter.notifyDataSetChanged();
        if (msgList.size() > 0){
            listView.setSelection(listView.getCount() - 1);
        }
        et_content.setText("");
        et_content.clearFocus();
    }

    EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            for (EMMessage message : list) {
                String username  = null;
                if(message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom){
                    username = message.getTo();
                }else{
                    //单聊消息
                    username = message.getFrom();
                }
                //如果是当前会话的消息，刷新聊天页面
                if (username.equals(toChatUsername)){
                    msgList.addAll(list);
                    adapter.notifyDataSetChanged();
                    if (msgList.size() > 0){
                        et_content.setSelection(listView.getCount() - 1);
                    }
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    @SuppressLint("InflateParams")
    class MessageAdapter extends BaseAdapter {
        private List<EMMessage> msgs;
        private Context context;
        private LayoutInflater inflater;

        public  MessageAdapter(List<EMMessage> msgs, Context context){
            this.msgs = msgs;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return msgs.size();
        }

        @Override
        public EMMessage getItem(int position) {
            return msgs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            EMMessage message = getItem(position);
            return message.direct() == EMMessage.Direct.RECEIVE ? 0 : 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @SuppressLint("InflateParams")

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            EMMessage message = getItem(position);
            int viewType = getItemViewType(position);
            if (convertView == null){
                if (viewType == 0){
                    convertView = inflater.inflate(R.layout.item_message_received, parent,false);
                }else {
                    convertView = inflater.inflate(R.layout.item_message_sent,parent,false);
                }
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if(holder == null){
                holder = new ViewHolder();
                holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
                convertView.setTag(holder);
            }
            EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
            holder.tv.setText(txtBody.getMessage());
            return  convertView;
        }
    }

    public static class ViewHolder{
        TextView tv;
    }
}
