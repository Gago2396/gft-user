package com.gfttraining.users;

import com.gfttraining.users.repository.UserRepository;
import com.gfttraining.users.model.PaymentMethod;
import com.gfttraining.users.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class UsersApplicationTests {


	private final UserRepository userRepository;

	@Autowired
	public UsersApplicationTests(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	@Test
	public void testSaveUser() {
		// Crear un PaymentMethod para asociarlo al usuario
		PaymentMethod paymentMethod = new PaymentMethod();
		paymentMethod.setName("Tarjeta de crédito");

		// Crear un usuario
		User user = new User();
		user.setName("John");
		user.setLastName("Doe");
		user.setAddress("123 Main St");
		user.setPaymentMethod(paymentMethod);
		user.setFidelityPoints(100);
		user.setAveragePurchase(50.0);

		// Guardar el usuario en la base de datos
		userRepository.save(user);

		// Verificar que el ID del usuario se ha generado
		assertNotNull(user.getId());
	}

	@Test
	public void testFindUserByName() {
		// Crear un usuario con un nombre específico
		User user = new User();
		user.setName("Alice");
		user.setLastName("Smith");
		user.setAddress("456 Oak St");
		user.setFidelityPoints(150);
		user.setAveragePurchase(75.0);

		// Guardar el usuario en la base de datos
		userRepository.save(user);

		// Buscar el usuario por nombre
		User foundUser = userRepository.findByName("Alice");

		// Verificar que el usuario no sea nulo y que tenga el mismo nombre
		assertNotNull(foundUser);
		assertEquals("Alice", foundUser.getName());
	}
}
