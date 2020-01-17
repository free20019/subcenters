package mvc.service;

import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

@Service
public class JmsErrorHandler implements ErrorHandler{

    public void handleError(Throwable throwable) {
        System.out.println("出错了！");
        throwable.printStackTrace();
    }
}
