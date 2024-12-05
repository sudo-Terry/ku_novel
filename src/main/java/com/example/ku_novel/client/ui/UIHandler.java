package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientListenerThread;
import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.client.model.ClientDataModel;
import com.example.ku_novel.client.ui.component.CustomAlert;
import com.example.ku_novel.common.Message;
import com.example.ku_novel.domain.NovelRoom;

import javax.swing.*;
import java.awt.*;
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
    private NovelRoomModalUI novelRoomModalUI;
    private NovelRoomCreateModalUI novelRoomCreateModalUI;
    private RoomSearchResultsModalUI roomSearchResultsModalUI;
    private NovelInputModalUI novelInputModalUI;
    private VoteModalUI voteModalUI;
    private NovelRoomSettingsModalUI novelRoomSettingsModalUI;
    private DownloadModalUI downloadModalUI;
    private RankingModalUI rankingModalUI;
    private ImageEditModalUI imageEditModalUI;
    private PwEditModalUI pwEditModalUI;
    private NameEditModalUI nameEditModalUI;


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

    public void showRoomSearchResults() {
        if (roomSearchResultsModalUI != null && roomSearchResultsModalUI.isVisible()) {
            roomSearchResultsModalUI.showRoomResult();
        }
    }

    public void showAlertModal(Component parentComponent, String title, String message, int messageType) {
        SwingUtilities.invokeLater(() -> {
            //JOptionPane.showMessageDialog(parentComponent, message, title, messageType)
            CustomAlert.showAlert((Window) parentComponent, title, message, null);
            }
        );
    }

    public void showNovelInputModal() {
        SwingUtilities.invokeLater(() -> {
            if(novelInputModalUI == null || !novelInputModalUI.isVisible()) {
                novelInputModalUI = new NovelInputModalUI(novelRoomModalUI);
                novelInputModalUI.setVisible(true);
            }
        });
    }

    public void showVoteModal() {
        SwingUtilities.invokeLater(() -> {
            if(voteModalUI == null || !voteModalUI.isVisible()) {
                voteModalUI = new VoteModalUI(novelRoomModalUI);
                voteModalUI.setVisible(true);
            }
        });
    }

    public void showNovelRoomModalUI(int roomId) {
        SwingUtilities.invokeLater(() -> {
            if (novelRoomModalUI == null || !novelRoomModalUI.isVisible()) {
                novelRoomModalUI = NovelRoomModalUI.getInstance();
                novelRoomModalUI.openModalWithRoomId(roomId);
            }else {
                CustomAlert.showAlert((Window) novelRoomModalUI, "경고", "이미 참여중인 소설방이 있습니다.", null);
            }
        });
    }

    public void repaintMainUI() {
        HomeUI.getInstance().repaintHomeUI();
    }

    public void repaintNovelRoomModalUI() {
        NovelRoomModalUI.getInstance().updateButtonArea();
    }

    public void updateNovelRoomChat(int roomId, String formattedMessage) {
        if(novelRoomModalUI.getRoomId() == ClientDataModel.getInstance().getCurrentRoomId())
            novelRoomModalUI.updateChatArea(formattedMessage);
        else
            System.out.println("novelRoomId 불일치로 채팅 업데이트 불가");
    }

    public void showNovelRoomSettingsModalUI(NovelRoomModalUI dialog) {
        SwingUtilities.invokeLater(() -> {
            if (novelRoomSettingsModalUI == null || !novelRoomSettingsModalUI.isVisible()) {
                novelRoomSettingsModalUI = new NovelRoomSettingsModalUI(dialog);
                novelRoomSettingsModalUI.setVisible(true);
            }else {
                System.out.println("NovelRoomSettingsModalUI 이미 열려 있음");
            }
        });
    }

    public void disposeNovelRoomSettingsModalUI() {
        SwingUtilities.invokeLater(() -> {
            if (novelRoomSettingsModalUI != null) {
                novelRoomSettingsModalUI.dispose();
                novelRoomSettingsModalUI = null;
            }else{
                System.out.println("NovelRoomSettingsModalUI가 null입니다.");
            }
        });
    }

    public void showDownloadModal(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            if (downloadModalUI == null || !downloadModalUI.isVisible()) {
                downloadModalUI = new DownloadModalUI(frame);
                downloadModalUI.showModal();
            }else {
                System.out.println("downloadModalUI 이미 열려 있음");
            }
        });
    }

    public void showAuthorAcceptModalUI(String nickname) {
        SwingUtilities.invokeLater(() -> {
            AuthorAcceptModalUI modal = new AuthorAcceptModalUI(null, nickname);
            modal.setVisible(true);
        });
    }

    public void repaintVoteModalUI() {
        SwingUtilities.invokeLater(() -> {
            voteModalUI = new VoteModalUI(novelRoomModalUI);
            voteModalUI.initializeTableData();
        });
    }

    public void setVoteModalUIVisible() {
        SwingUtilities.invokeLater(() -> {
            if (voteModalUI != null) {
                voteModalUI.setVisible(true);
            }
        });
    }

    public void showRankingModal(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            if (rankingModalUI == null || !rankingModalUI.isVisible()) {
                rankingModalUI = new RankingModalUI(frame);
                rankingModalUI.showModal();
            }else {
                System.out.println("rankingModalUI 이미 열려 있음");
            }
        });
    }

    public void showImageEditModal(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            if (imageEditModalUI == null || !imageEditModalUI.isVisible()) {
                imageEditModalUI = new ImageEditModalUI(frame);
                imageEditModalUI.showModal();
            }else {
                System.out.println("imageEditModalUI 이미 열려 있음");
            }
        });
    }

    public void showPwEditModal(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            if (pwEditModalUI == null || !pwEditModalUI.isVisible()) {
                pwEditModalUI = new PwEditModalUI(frame);
                pwEditModalUI.showModal();
            }else {
                System.out.println("pwEditModalUI 이미 열려 있음");
            }
        });
    }

    public void showNameEditModal(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            if (nameEditModalUI == null || !nameEditModalUI.isVisible()) {
                nameEditModalUI = new NameEditModalUI(frame);
                nameEditModalUI.showModal();
            }else {
                System.out.println("nameEditModalUI 이미 열려 있음");
            }
        });
    }

    public void updateNovelRoomParticipantsCount() {
        if (novelRoomModalUI != null)
            novelRoomModalUI.updateParticipantButton();
    }
}
