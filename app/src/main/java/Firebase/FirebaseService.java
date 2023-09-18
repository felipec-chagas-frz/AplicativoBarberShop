package Firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage notificacao) {
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}
