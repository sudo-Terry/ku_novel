package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientListenerThread;
import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.common.Message;
import com.example.ku_novel.domain.NovelRoom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UIHandler {
    private static UIHandler instance;
    private Socket socket;
    private PrintWriter writer;
    private ConcurrentLinkedQueue<Message> messageQueue = new ConcurrentLinkedQueue<>();
    private LoginUI loginUI;
    private SignUpModalUI signUpModalUI;
    private NovelRoomCreateModalUI novelRoomCreateModalUI;
    private RoomSearchResultsModalUI roomSearchResultsModalUI;
    private NovelInputModalUI novelInputModalUI;
    private VoteModalUI voteModalUI;

    // 생성자에서 데몬 스레드 실행
    public UIHandler() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            socket = new Socket("127.0.0.1", 10100);
            System.out.println("[Client] Connected to server.");

            // 서버 요청 수신 스레드
            ClientListenerThread clientListenerThread = new ClientListenerThread(socket, messageQueue);
            clientListenerThread.start();

            // 서버 요청 전송 스레드
            ClientSenderThread.initialize(socket);
            ClientSenderThread.getInstance().start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }/*finally {
            try {
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }*/
    }

    public static UIHandler getInstance() {
        if (instance == null) {
            instance = new UIHandler();
        }
        return instance;
    }

    public void showLoginUI() {
        SwingUtilities.invokeLater(() -> {
            if (loginUI == null || !loginUI.isVisible()) { // 이미 창이 열려 있지 않을 때만 새로 생성
                loginUI = new LoginUI();
            }
        });
    }

    public void disposeLoginUI() {
        SwingUtilities.invokeLater(() -> {
            if (loginUI != null) { // 로그인 UI가 존재하면 닫기
                loginUI.dispose();
                loginUI = null; // 참조 해제
            }
        });
    }

    public void showSignUpModalUI(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            if (signUpModalUI == null || !signUpModalUI.isVisible()) {
                signUpModalUI = new SignUpModalUI(frame);
                signUpModalUI.setVisible(true);
            }else {
                System.out.println("SignUpModalUI 이미 열려 있음");
            }
        });
    }

    public void disposeSignUpModalUI() {
        SwingUtilities.invokeLater(() -> {
            if (signUpModalUI != null) {
                signUpModalUI.dispose();
                signUpModalUI = null;
            }else{
                System.out.println("SignUpModalUI가 null입니다.");
            }
        });
    }

    public void showNovelRoomCreateModalUI(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            if (novelRoomCreateModalUI == null || !novelRoomCreateModalUI.isVisible()) {
                novelRoomCreateModalUI = new NovelRoomCreateModalUI(frame);
                novelRoomCreateModalUI.setVisible(true);
            }else {
                System.out.println("NovelRoomCreateModalUI 이미 열려 있음");
            }
        });
    }

    public void disposeNovelRoomCreateModalUI() {
        SwingUtilities.invokeLater(() -> {
            if (novelRoomCreateModalUI != null) {
                novelRoomCreateModalUI.dispose();
                novelRoomCreateModalUI = null;
            }else{
                System.out.println("NovelRoomCreateModalUI null입니다.");
            }
        });
    }

    public void showRoomSearchModal(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            if (roomSearchResultsModalUI == null || !roomSearchResultsModalUI.isVisible()) {
                roomSearchResultsModalUI = new RoomSearchResultsModalUI(frame);
                roomSearchResultsModalUI.showModal();
            }else {
                System.out.println("showRoomSearchModal 이미 열려 있음");
            }
        });
    }

    public void showRoomSearchResults(NovelRoom[] rooms) {
        if (roomSearchResultsModalUI != null && roomSearchResultsModalUI.isVisible()) {
            roomSearchResultsModalUI.showRoomResult(rooms);
        }
    }

    public void showAlertModal(Component parentComponent, String title, String message, int messageType) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(parentComponent, message, title, messageType)
        );
    }

    public void showNovelInputModal(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            if(novelInputModalUI == null || !novelInputModalUI.isVisible()) {
                novelInputModalUI = new NovelInputModalUI(frame);
                novelInputModalUI.setVisible(true);
            }
        });
    }

    public void showVoteModal(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            if(voteModalUI == null || !voteModalUI.isVisible()) {
                voteModalUI = new VoteModalUI(frame);
                voteModalUI.setVisible(true);
            }
        });
    }


}
