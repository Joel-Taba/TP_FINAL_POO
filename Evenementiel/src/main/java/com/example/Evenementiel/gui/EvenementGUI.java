package com.example.Evenementiel.gui;

import com.example.Evenementiel.Entities.Abstract.Evenement;
import com.example.Evenementiel.Entities.Concrete.Concert;
import com.example.Evenementiel.Entities.Concrete.Conference;
import com.example.Evenementiel.Entities.Concrete.Participant;
import com.example.Evenementiel.Utils.EvenementSerializer;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EvenementGUI extends JFrame {
    private List<Evenement> evenements = new ArrayList<>();
    private JTextArea affichage;

    private JTextField nomField;
    private JTextField lieuField;
    private JTextField capaciteField;

    private JTextField artisteField;
    private JTextField genreField;
    private JTextField themeField;
    private JTextField intervenantsField;

    private JComboBox<String> typeBox;
    private JPanel champsSpecifiques;

    public EvenementGUI() {
        setTitle("Créer un Événement");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        affichage = new JTextArea();
        affichage.setEditable(false);
        mainPanel.add(new JScrollPane(affichage), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(10, 2));
        formPanel.add(new JLabel("Type :"));
        typeBox = new JComboBox<>(new String[]{"Concert", "Conférence"});
        formPanel.add(typeBox);

        formPanel.add(new JLabel("Nom :"));
        nomField = new JTextField();
        formPanel.add(nomField);

        formPanel.add(new JLabel("Lieu :"));
        lieuField = new JTextField();
        formPanel.add(lieuField);

        formPanel.add(new JLabel("Capacité :"));
        capaciteField = new JTextField();
        formPanel.add(capaciteField);

        champsSpecifiques = new JPanel(new GridLayout(2, 2));
        artisteField = new JTextField();
        genreField = new JTextField();
        themeField = new JTextField();
        intervenantsField = new JTextField();
        updateChampsSpecifiques();
        formPanel.add(champsSpecifiques);

        typeBox.addActionListener(e -> updateChampsSpecifiques());

        JButton creerBtn = new JButton("Créer l'événement");
        creerBtn.addActionListener(e -> creerEvenement());
        formPanel.add(creerBtn);

        JButton sauvegarderBtn = new JButton("Sauvegarder en JSON");
        sauvegarderBtn.addActionListener(e -> sauvegarderEvenements());
        formPanel.add(sauvegarderBtn);

        JButton annulerBtn = new JButton("Annuler le dernier événement");
        annulerBtn.addActionListener(e -> annulerDernierEvenement());
        formPanel.add(annulerBtn);

        mainPanel.add(formPanel, BorderLayout.NORTH);
        add(mainPanel);
        setVisible(true);
    }

    private void updateChampsSpecifiques() {
        champsSpecifiques.removeAll();
        if ("Concert".equals(typeBox.getSelectedItem())) {
            champsSpecifiques.add(new JLabel("Artiste :"));
            champsSpecifiques.add(artisteField);
            champsSpecifiques.add(new JLabel("Genre musical :"));
            champsSpecifiques.add(genreField);
        } else {
            champsSpecifiques.add(new JLabel("Thème :"));
            champsSpecifiques.add(themeField);
            champsSpecifiques.add(new JLabel("Intervenants (séparés par ,) :"));
            champsSpecifiques.add(intervenantsField);
        }
        champsSpecifiques.revalidate();
        champsSpecifiques.repaint();
    }

    private void creerEvenement() {
        try {
            String id = "E" + (evenements.size() + 1);
            String nom = nomField.getText();
            String lieu = lieuField.getText();
            int capacite = Integer.parseInt(capaciteField.getText());
            LocalDateTime date = LocalDateTime.now();

            Evenement ev;
            if ("Concert".equals(typeBox.getSelectedItem())) {
                String artiste = artisteField.getText();
                String genre = genreField.getText();
                ev = new Concert(id, nom, date, lieu, capacite, artiste, genre);
            } else {String theme = themeField.getText();
                List<String> intervenants = List.of(intervenantsField.getText().split(","));
                ev = new Conference(id, nom, date, lieu, capacite, theme, intervenants);
            }

            // Ajouter un participant de test
            ev.ajouterParticipant(new Participant("P1", "Alice", "alice@example.com"));

            evenements.add(ev);
            affichage.append("Ajouté : " + nom + " [" + typeBox.getSelectedItem() + "]\n");

            nomField.setText("");
            lieuField.setText("");
            capaciteField.setText("");
            artisteField.setText("");
            genreField.setText("");
            themeField.setText("");
            intervenantsField.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sauvegarderEvenements() {
        try {
            String chemin = "evenements_gui.json";
            EvenementSerializer.sauvegarderEvenements(evenements, chemin);
            JOptionPane.showMessageDialog(this, "Événements sauvegardés dans : " + chemin);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void annulerDernierEvenement() {
        if (evenements.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun événement à annuler.");
            return;
        }

        Evenement dernier = evenements.get(evenements.size() - 1);
        dernier.annuler();

        affichage.append(">>> ANNULATION : " + dernier.getNom() + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EvenementGUI::new);
    }
}