package org.entdes.todolist;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorTasques {
    private List<Tasca> llista = new ArrayList<>();

    public int afegirTasca(String descripcio) throws Exception {
        validarSiExisteixTasca(descripcio);
        Tasca tasca = new Tasca(descripcio);
        llista.add(tasca);
        return tasca.getId();
    }

    private void validarSiExisteixTasca(String descripcio) throws Exception {

        for (Tasca tasca : llista) {
            if (tasca.getDescripcio().equalsIgnoreCase(descripcio)) {
                throw new Exception("La tasca ja existeix");
            }
        }

    }

    public void eliminarTasca(int id) {
        for (int i = 0; i < llista.size(); i++) {
            if (llista.get(i).getId() == id) {
                llista.remove(i);
                break;
            }
        }
    }

    public void marcarCompletada(int id) throws Exception {
        Tasca tascaModificada = null;
        for (Tasca tasca : llista) {
            if (tasca.getId() == id) {
                tasca.setCompletada(true);
                break;
            }
        }
        if (tascaModificada == null)
            throw new Exception("La tasca no existeix");        
    }

    public void modificarTasca(int id, String novaDescripcio, Boolean completada) throws Exception {
        validarSiExisteixTasca(novaDescripcio);
        Tasca tascaModificada = null;
        for (Tasca tasca : llista) {
            if (tasca.getId() == id) {
                tasca.setDescripcio(novaDescripcio);
                tasca.setCompletada(completada == null ? false : completada);
                tascaModificada = tasca;
                break;
            }
        }
        if (tascaModificada == null)
            throw new Exception("La tasca no existeix");

    }

    public Tasca obtenirTasca(int id) throws Exception {
        for (Tasca tasca : llista) {
            if (tasca.getId() == id) {
                return tasca;
            }
        }
        throw new Exception("La tasca no existeix");
    }

    public int getNombreTasques() {
        return llista.size();
    }

    public List<Tasca> llistarTasques() {
        return llista;
    }

    public List<Tasca> llistarTasquesPerDescripcio(String filtreDescripcio) {

        return filtrarPerDescripcio(filtreDescripcio, llistarTasques());
    }

    private List<Tasca> filtrarPerDescripcio(String filtreDescripcio, List<Tasca> tasques) {
        List<Tasca> tasquesFiltrades = new ArrayList<>();
        for (Tasca tasca : tasques) {
            if (tasca.getDescripcio().toLowerCase().contains(filtreDescripcio.toLowerCase())) { // hauria de ser tasca.getDescripcio().contains(...)
                tasquesFiltrades.add(tasca);
            }
        }
        return tasquesFiltrades;
    }

    // Persistència amb serialització
    private static final String FITXER_DADES = "tasques.dat";

    public void guardar() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FITXER_DADES))) {
            oos.writeObject(llista);
        } catch (IOException e) {
            System.err.println("Error guardant tasques: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void carregar() {
        File fitxer = new File(FITXER_DADES);
        if (fitxer.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(fitxer))) {
                llista = (List<Tasca>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error carregant tasques: " + e.getMessage());
                llista = new ArrayList<>();
            }
        }
    }
}