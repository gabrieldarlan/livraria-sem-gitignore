package br.com.caelum.livraria.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.util.RedirectView;

@ManagedBean
@ViewScoped
public class LivroBean {

	private Livro livro = new Livro();

	private Integer autorId;

	public Livro getLivro() {
		return livro;
	}

	
	private List<Livro> livrosRetornados = new ArrayList<Livro>();
	
	public List<Livro> getLivrosRetornados() {
		return livrosRetornados;
	}
	
	public void setLivrosRetornados(List<Livro> livrosRetornados) {
		this.livrosRetornados = livrosRetornados;
	}
	
	public void gravarAutor() {
		Autor autor = new DAO<Autor>(Autor.class).buscaPorId(autorId);
		this.livro.adicionaAutor(autor);
	}

	public List<Autor> getAutores() {
		return new DAO<Autor>(Autor.class).listaTodos();
	}

	public List<Autor> getAutoresDoLivro() {
		return this.livro.getAutores();
	}

	public List<Livro> getLivros() {
		return livrosRetornados = new DAO<Livro>(Livro.class).listaTodos();
	}

	public void gravar() {
//		System.out.println("Gravando livro " + this.livro.getTitulo());
		FacesContext context = FacesContext.getCurrentInstance();

		if (livro.getAutores().isEmpty()) {
			context.addMessage("autor", new FacesMessage("O autor é obrigatório"));
		}

		if(this.livro.getId()==null) {
			new DAO<Livro>(Livro.class).adiciona(this.livro);
		} else {
			new DAO<Livro>(Livro.class).atualiza(this.livro);
		}

		this.livro = new Livro();
	}

	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public void comecaComDigitoUm(FacesContext tc, UIComponent component, Object value) throws ValidatorException {
		String valor = value.toString();
		if (!valor.startsWith("1")) {
			throw new ValidatorException(new FacesMessage("ISBN deve começar com o número"));
		}
	}

	public RedirectView formAutor() {
		return new RedirectView("autor");
	}

	public void remove(Livro livro) {
		try {
			new DAO<Livro>(Livro.class).remove(livro);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro na remoção do livro");
		}
	}

	public void carregar(Livro livro) {
		this.livro = livro;
	}
	
	public void removeAutorDoLivro(Autor autor) {
		this.livro.remove(autor);
	}
	
}
