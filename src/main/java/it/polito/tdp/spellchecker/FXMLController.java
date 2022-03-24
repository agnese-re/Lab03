/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.spellchecker;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.spellchecker1.model.Dictionary;
import it.polito.tdp.spellchecker1.model.RichWord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class FXMLController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="cmbLanguage"
    private ComboBox<String> cmbLanguage; // Value injected by FXMLLoader

    @FXML // fx:id="txtDaCorreggere"
    private TextArea txtDaCorreggere; // Value injected by FXMLLoader

    @FXML // fx:id="txtRisultato"
    private TextArea txtRisultato; // Value injected by FXMLLoader
    
    @FXML // fx:id="txtStato"
    private Label txtStato; // Value injected by FXMLLoader
    
    @FXML // fx:id="txtPerformance"
    private Label txtPerformance; // Value injected by FXMLLoader
    
    @FXML // fx:id="btnClearText"
    private Button btnClearText; // Value injected by FXMLLoader

    @FXML // fx:id="btnSpellCheck"
    private Button btnSpellCheck; // Value injected by FXMLLoader
    
    private Dictionary model;

    @FXML
    void handleActivation(ActionEvent event) {
    	if(this.cmbLanguage.getValue() != null) {
    		txtDaCorreggere.setDisable(false);
    		txtRisultato.setDisable(false);
    		btnSpellCheck.setDisable(false);
    		btnClearText.setDisable(false);
    		txtDaCorreggere.clear();
    		txtRisultato.clear();
    	}
    }
    
    @FXML
    void handleClearText(ActionEvent event) {
    	txtDaCorreggere.clear();
    	txtRisultato.clear();
    	txtStato.setText("");
    	txtPerformance.setText("");
    	model.clearAll();
    }

    @FXML
    void handleSpellCheck(ActionEvent event) {
    	// Ricavare la lingua selezionata
    	String language = this.cmbLanguage.getValue();
    	// Caricare il dizionario relativo alla lingua scelta
    	model.loadDictionary(language);
    	txtRisultato.appendText("Dim. vocabolario = " + model.dimDizionario() + " parole\n"); // 893249 (IT), 51533 (EN)
    	// Logica -> controllo se la parola appartiene al dizionario
    	String testoDaCorreggere = txtDaCorreggere.getText();
    	testoDaCorreggere = testoDaCorreggere.replaceAll("[.,\\/#!?$%\\^&\\*;:{}=\\-_`~()\\[\\]\"]", "");	// elimino segni di punteggiatura
    	String array[] = testoDaCorreggere.split(" ");	// oppure StringTokenizer
    	List<String> input = new LinkedList<String>();
    	for(String s: array)
    		input.add(s);
    	// Visualizzo risultato -> RichWord con campo boolean a false
    	long start = System.nanoTime();
    	List<RichWord> paroleErrate = model.spellCheckTextLinear(input);
    	long end = System.nanoTime();
    	long res = end - start;
    	for(RichWord rw: paroleErrate)
    		if(rw.isCorretta() == false)
    			txtRisultato.appendText(rw.getParola() + "\n");
    	txtStato.setText("The text contains " + model.numeroErrori() + " errors");
    	txtPerformance.setText("Spell check completed in " + (end - start)/Math.pow(10,9) + " seconds");
    	
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert cmbLanguage != null : "fx:id=\"cmbLanguage\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDaCorreggere != null : "fx:id=\"txtDaCorreggere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtRisultato != null : "fx:id=\"txtRisultato\" was not injected: check your FXML file 'Scene.fxml'.";
        this.cmbLanguage.getItems().addAll("English", "Italian");
    }

    public void setModel(Dictionary model) {
    	this.model = model;
    }
    
}
