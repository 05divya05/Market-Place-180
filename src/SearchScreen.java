import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.ArrayList;

public class SearchScreen extends JPanel {

    private PrintWriter writer;
    private ObjectInputStream objectReader;
    private AppGUI appGUI;
    private UserProfile user;
    private JTextField userToSearch;
    public JPanel displayPanel;
    private JScrollPane displayScrollPane;
    private BufferedReader reader;

    public SearchScreen(PrintWriter writer, ObjectInputStream objectReader, AppGUI gui, UserProfile userProfile) {
        this.appGUI = gui;
        this.user = userProfile;
        this.objectReader = objectReader;
        this.writer = writer;

        setLayout(new BorderLayout());
        JLabel label = new JLabel("Search Screen", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        userToSearch = new JTextField("Enter Username Here");
        userToSearch.setMaximumSize(new Dimension(300, 40));
        userToSearch.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchPanel.add(userToSearch);

        JButton searchButton = new JButton("Search");
        searchButton.setMaximumSize(new Dimension(300, 40));
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchButton.addActionListener(new SearchLoginListener());
        searchPanel.add(searchButton);

        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        displayPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(searchPanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);
        add(createNavBar(), BorderLayout.SOUTH);
    }

    private class SearchLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                writer.println(userToSearch.getText());
                try {
                    Object result = objectReader.readObject();

                    if (result instanceof String) {
                        JOptionPane.showMessageDialog(SearchScreen.this,
                                "User doesn't exist", "Search Error", JOptionPane.ERROR_MESSAGE);
                    } else if (result instanceof UserProfile) {
                        UserProfile searchedUser = (UserProfile) result;
                        displayUserInfo(searchedUser);

                        JPanel postsPanel = new JPanel();
                        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
                        for (NewsPost post : searchedUser.getUserPosts()) {
                            postsPanel.add(searchPostPanel(post));
                        }

                        displayScrollPane = new JScrollPane(postsPanel);
                        displayScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                        displayScrollPane.setMinimumSize(new Dimension(displayPanel.getWidth(), 300));
                        displayScrollPane.setMaximumSize(new Dimension(displayPanel.getWidth(), 300));

                        displayPanel.add(displayScrollPane, BorderLayout.CENTER);
                        displayPanel.revalidate();
                        displayPanel.repaint();
                    }

                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }

    private void displayUserInfo(UserProfile user) {
        displayPanel.removeAll();

        JLabel userNameLabel = new JLabel("Profile: " + user.getUsername());
        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        JLabel friendsLabel = new JLabel("Friends: " +
                (user.getFriends().getFirst().equals("EmptyFriendsList") ? 0 : user.getFriends().size()));
        friendsLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        JLabel postsLabel = new JLabel("Posts: " + user.getUserPosts().size());
        postsLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        infoPanel.add(friendsLabel);
        infoPanel.add(postsLabel);

        displayPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        displayPanel.add(userNameLabel);
        displayPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        displayPanel.add(infoPanel);
        displayPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        displayPanel.revalidate();
        displayPanel.repaint();
    }

    private JPanel searchPostPanel(NewsPost post) {
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        postPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        postPanel.setBackground(new Color(230, 230, 230));

        ImageIcon originalIcon = new ImageIcon(post.getImagePath());
        Image resizedImage = originalIcon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(resizedImage));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel captionLabel = new JLabel("@" + post.getAuthor() + " - " + post.getCaption());
        captionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel commentPanel = new JPanel(new FlowLayout());
        JButton commentButton = new JButton("Comment");
        JButton viewCommentsButton = new JButton("View Comments");

        commentButton.addActionListener(e -> {
            String comment = JOptionPane.showInputDialog(postPanel, "Enter comment:", "Comment", JOptionPane.PLAIN_MESSAGE);
            if (comment != null && !comment.trim().isEmpty()) {
                writer.println("COMMENT");
                writer.println(post.getCaption());
                writer.println(comment);
                writer.println("1");
            }
        });

        viewCommentsButton.addActionListener(e -> {
            writer.println("VIEW");
            ArrayList<NewsComment> comments = NewsPost.findComments(post.getCaption());

            JDialog dialog = new JDialog((Frame) null, "Comments", true);
            dialog.setSize(250, 300);
            dialog.setLayout(new BorderLayout());

            JPanel commentsPanel = new JPanel();
            commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));

            if (comments.isEmpty()) {
                commentsPanel.add(new JLabel("No comments yet."));
            } else {
                for (NewsComment c : comments) {
                    commentsPanel.add(createCommentPanel(c));
                    commentsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }

            dialog.add(new JScrollPane(commentsPanel), BorderLayout.CENTER);
            dialog.setVisible(true);
            writer.println("1");
        });

        commentPanel.add(commentButton);
        commentPanel.add(viewCommentsButton);

        postPanel.add(imageLabel);
        postPanel.add(captionLabel);
        postPanel.add(commentPanel);
        return postPanel;
    }

    private JPanel createCommentPanel(NewsComment comment) {
        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
        commentPanel.setBackground(new Color(245, 245, 245));
        commentPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel author = new JLabel("Author: " + comment.getAuthor());
        JLabel content = new JLabel(comment.getContent());
        JLabel votes = new JLabel("Upvotes: " + comment.getUpvotes() + " | Downvotes: " + comment.getDownvotes());

        commentPanel.add(author);
        commentPanel.add(content);
        commentPanel.add(votes);
        return commentPanel;
    }

    private JPanel createNavBar() {
        JPanel navBar = new JPanel();
        navBar.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        navBar.setBackground(Color.WHITE);

        String[] icons = {"searchIcon.png", "contentIcon.png", "homeIcon.png", "friendsIcon.png", "profileIcon.png"};

        JPanel[] pages = {
                new SearchScreen(writer, objectReader, appGUI, user),
                new ContentScreen(reader,writer, objectReader, user),
                new HomeScreen(reader, objectReader, writer, appGUI, user),
                new FriendScreen(reader, writer, objectReader, appGUI, user),
                new ProfileScreen (reader, writer,objectReader,appGUI,user),  // Replace with actual ProfileScreen
        };

        for (int i = 0; i < icons.length; i++) {
            ImageIcon icon = new ImageIcon("images/navBarIcons/" + icons[i]);
            Image scaled = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            JButton navButton = new JButton(new ImageIcon(scaled));
            navButton.setFocusPainted(false);
            navButton.setContentAreaFilled(false);
            navButton.setBorder(BorderFactory.createEmptyBorder());

            final JPanel targetPage = pages[i];
            navButton.addActionListener(e -> appGUI.showPage(targetPage));

            navBar.add(navButton);
        }

        return navBar;
    }
}
