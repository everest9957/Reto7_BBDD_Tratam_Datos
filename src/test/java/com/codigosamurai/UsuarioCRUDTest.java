package com.codigosamurai;

import com.codigosamurai.dao.UsuarioDAO;
import com.codigosamurai.entity.Usuario;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioCRUDTest {

    private static UsuarioDAO dao;

    @BeforeAll
    static void setup() {
        dao = new UsuarioDAO();
    }

    @Test
    @Order(1)
    void testGuardarUsuario() {
        Usuario u = new Usuario("Kenshin", "kenshin@samurai.com");
        dao.guardar(u);
        assertNotNull(u.getId(), "El ID debe generarse tras persistir");
    }

    @Test
    @Order(2)
    void testObtenerTodos() {
        List<Usuario> usuarios = dao.obtenerTodos();
        assertFalse(usuarios.isEmpty(), "Debe haber al menos un usuario");
    }

    @Test
    @Order(3)
    void testActualizarUsuario() {
        List<Usuario> usuarios = dao.obtenerTodos();
        Usuario u = usuarios.get(0);
        u.setNombre("Kenshin Himura");
        dao.actualizar(u);

        Usuario actualizado = dao.obtenerPorId(u.getId());
        assertEquals("Kenshin Himura", actualizado.getNombre());
    }

    @Test
    @Order(4)
    void testEliminarUsuario() {
        List<Usuario> usuarios = dao.obtenerTodos();
        Long id = usuarios.get(0).getId();
        dao.eliminar(id);
        assertNull(dao.obtenerPorId(id), "El usuario debe haber sido eliminado");
    }
}
