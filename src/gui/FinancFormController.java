package gui;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;
import model.entities.Aluno;
import model.exceptions.ValidationException;
import model.services.AlunoService;
import model.services.PlanoService;

public class FinancFormController implements Initializable {

	protected Aluno entity;

	private AlunoService service;
	
	private PlanoService planoService;
	
	private String tabelaAluno;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	protected TextField txtId;

	@FXML
	protected TextField txtNome;

	@FXML
	protected TextField txtTelefone;
	
	@FXML
	protected DatePicker dpPagamento;
	
	@FXML
	protected DatePicker dpReferencia;
	
	@FXML
	protected DatePicker dpVencimento;
	
	@FXML
	protected TextField txtMensalidade;
	
	@FXML
	protected TextArea txtObserv;	

	@FXML
	protected Button btnConfirm;

	@FXML
	protected Button btnCancel;
	

	public void setAluno(Aluno entity) {
		this.entity = entity;
	}

	public void setServices(AlunoService service, PlanoService planoService) {
		this.service = service;
		this.planoService = planoService;
	}
	
	public void setTabelas(String tabelaAluno) {
		this.tabelaAluno = tabelaAluno;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtnConfirmAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		if (service == null) {
			throw new IllegalStateException("Servi�o nulo");
		}
		try {
			entity = getFormData();
			service.updatePagamento(entity, tabelaAluno);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (DBException e) {
			Alerts.showAlert("Erro ao salvar o objeto", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidationException e) {
			e.printStackTrace();
		}

	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Aluno getFormData() {
		Aluno obj = new Aluno();
		ValidationException exception = new ValidationException("Erro de valida��o");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "Campo obrigat�rio.");
		}
		obj.setNome(txtNome.getText());

		if (txtTelefone.getText() == null || txtTelefone.getText().trim().equals("")) {
			exception.addError("email", "Campo obrigat�rio.");
		}
		obj.setTelefone(txtTelefone.getText());

		if (dpPagamento.getValue() != null) {
			Instant instant = Instant.from(dpPagamento.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setPagamento(Date.from(instant));
		}
		
		if (dpReferencia.getValue() != null) {
			Instant instant = Instant.from(dpReferencia.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setReferencia(Date.from(instant));
		}
		
		if (dpVencimento.getValue() != null) {
			Instant instant = Instant.from(dpVencimento.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setVencimento(Date.from(instant));
		}
		
		if (txtMensalidade.getText() != null) {
			obj.setMensalidade(Utils.tryParseToDouble(txtMensalidade.getText()));
		}
		
		if (txtObserv.getText() != null) {
			obj.setObserv(txtObserv.getText());
		}
				
		return obj;
	}
	
	
	@FXML
	private void validateDatePickers() {
		Robot robot = new Robot();
		dpPagamento.requestFocus();
		robot.keyType(KeyCode.ENTER);
		robot.keyType(KeyCode.TAB);
		robot.keyType(KeyCode.ENTER);
		robot.keyType(KeyCode.TAB);
		robot.keyType(KeyCode.ENTER);
	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Constraints.setTextFieldMaxLength(txtTelefone, 20);
		Constraints.setTextFieldMaxLength(txtTelefone, 70);
		Utils.enhanceDatePickers(dpPagamento, dpReferencia, dpVencimento);
		Utils.formatDatePicker(dpPagamento, "dd/MM/yyyy");
		Utils.formatDatePicker(dpReferencia, "dd/MM/yyyy");
		Utils.formatDatePicker(dpVencimento, "dd/MM/yyyy");		
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula.");
		}
		txtId.setText(entity.getId() == null ? "" : String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtTelefone.setText(entity.getTelefone());
		if (entity.getVencimento() != null) {
			java.util.Date referencia = new Date(entity.getVencimento().getTime());
			dpReferencia.setValue(referencia.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
		txtMensalidade.setText(entity.getMensalidade() == null? "" : String.valueOf(entity.getMensalidade()));
		txtObserv.setText(entity.getObserv());
		
	}
	
	public void loadAssociatedObjects() {
		if (planoService == null) {
			throw new IllegalStateException("Servi�o de plano est� nulo.");
		}
	}
}
