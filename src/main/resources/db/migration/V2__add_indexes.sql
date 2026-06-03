-- Índices para otimização de buscas
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_order_status ON orders(order_status);
CREATE INDEX idx_addresses_user_id ON addresses(user_id);

-- Constraints de unicidade (Garantia de integridade)
ALTER TABLE cart_items ADD CONSTRAINT uk_cart_product UNIQUE (cart_id, product_id);
ALTER TABLE categories ADD CONSTRAINT uk_category_name UNIQUE (name);