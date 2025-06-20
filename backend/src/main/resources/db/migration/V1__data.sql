-- roles table
CREATE TABLE IF NOT EXISTS public.roles (
    role_id BIGSERIAL  PRIMARY KEY,
    created_at timestamp(6) without time zone,
    created_by character varying(255),
    last_modified_at timestamp(6) without time zone,
    last_modified_by character varying(255),
    is_deleted boolean NOT NULL,
    role_name character varying(20) NOT NULL,
    CONSTRAINT roles_role_name_check CHECK (
        role_name IN ('CUSTOMER', 'EMPLOYEE', 'ADMIN', 'WAITER', 'CHEF', 'RECEPTIONIST')
    )
);

-- payment methods
CREATE TABLE IF NOT EXISTS public.payment_methods (
    method_id character varying(36) NOT NULL PRIMARY KEY,
    created_at timestamp(6) without time zone,
    created_by character varying(255),
    last_modified_at timestamp(6) without time zone,
    last_modified_by character varying(255),
    details text,
    method_name character varying(20) NOT NULL
);
-- payment method types
CREATE TABLE IF NOT EXISTS public.payment_method_types (
    id BIGSERIAL  PRIMARY KEY,
    created_at timestamp(6) without time zone,
    created_by character varying(255),
    last_modified_at timestamp(6) without time zone,
    last_modified_by character varying(255),
    is_deleted boolean NOT NULL,
    name character varying(30) NOT NULL
);
-- payment method types _ payments
CREATE TABLE IF NOT EXISTS public.payment_method_types_payments (
    type_id bigint NOT NULL,
    method_id character varying(36) NOT NULL
);
-- topics
CREATE TABLE IF NOT EXISTS public.topics (
    topic_id BIGSERIAL PRIMARY KEY,
    created_at timestamp(6) without time zone,
    created_by character varying(255),
    last_modified_at timestamp(6) without time zone,
    last_modified_by character varying(255),
    is_deleted boolean,
    topic_name character varying(20) NOT NULL
);

-- insert roles
INSERT INTO public.roles (created_at, created_by, last_modified_at, last_modified_by, is_deleted, role_name)
SELECT CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', false, 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM public.roles WHERE role_name = 'ADMIN');

INSERT INTO public.roles (created_at, created_by, last_modified_at, last_modified_by, is_deleted, role_name)
SELECT CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', false, 'CUSTOMER'
WHERE NOT EXISTS (SELECT 1 FROM public.roles WHERE role_name = 'CUSTOMER');

INSERT INTO public.roles (created_at, created_by, last_modified_at, last_modified_by, is_deleted, role_name)
SELECT CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', false, 'CHEF'
WHERE NOT EXISTS (SELECT 1 FROM public.roles WHERE role_name = 'CHEF');

INSERT INTO public.roles (created_at, created_by, last_modified_at, last_modified_by, is_deleted, role_name)
SELECT CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', false, 'WAITER'
WHERE NOT EXISTS (SELECT 1 FROM public.roles WHERE role_name = 'WAITER');

INSERT INTO public.roles (created_at, created_by, last_modified_at, last_modified_by, is_deleted, role_name)
SELECT CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', false, 'EMPLOYEE'
WHERE NOT EXISTS (SELECT 1 FROM public.roles WHERE role_name = 'EMPLOYEE');

INSERT INTO public.roles (created_at, created_by, last_modified_at, last_modified_by, is_deleted, role_name)
SELECT CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', false, 'RECEPTIONIST'
WHERE NOT EXISTS (SELECT 1 FROM public.roles WHERE role_name = 'RECEPTIONIST');

-- insert payment methods
INSERT INTO public.payment_methods (method_id, created_at, created_by, last_modified_at, last_modified_by, details, method_name)
SELECT '9e86d6db-2e17-481d-b82f-6b033e76086f', CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'Pay with cash when your order arrives at your doorstep.', 'cod'
WHERE NOT EXISTS (SELECT 1 FROM public.payment_methods WHERE method_name = 'cod');

INSERT INTO public.payment_methods (method_id, created_at, created_by, last_modified_at, last_modified_by, details, method_name)
SELECT 'd2125df0-2889-4e74-b264-8c4d0c04ab85', CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'Securely pay online using your PayPal account or linked cards.', 'paypal'
WHERE NOT EXISTS (SELECT 1 FROM public.payment_methods WHERE method_name = 'paypal');

INSERT INTO public.payment_methods (method_id, created_at, created_by, last_modified_at, last_modified_by, details, method_name)
SELECT 'fa36ab99-c173-4ded-a09f-a9910d630bcc', CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'Pay with physical currency', 'cash'
WHERE NOT EXISTS (SELECT 1 FROM public.payment_methods WHERE method_name = 'cash');

-- insert payment method types
INSERT INTO public.payment_method_types (created_at, created_by, last_modified_at, last_modified_by, is_deleted, "name")
SELECT CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', false, 'reservation'
WHERE NOT EXISTS (SELECT 1 FROM public.payment_method_types WHERE "name" = 'reservation');

INSERT INTO public.payment_method_types (created_at, created_by, last_modified_at, last_modified_by, is_deleted, "name")
SELECT CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', false, 'dine_in'
WHERE NOT EXISTS (SELECT 1 FROM public.payment_method_types WHERE "name" = 'dine_in');

INSERT INTO public.payment_method_types (created_at, created_by, last_modified_at, last_modified_by, is_deleted, "name")
SELECT CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', false, 'take_away'
WHERE NOT EXISTS (SELECT 1 FROM public.payment_method_types WHERE "name" = 'take_away');

-- insert payment method types payments
INSERT INTO public.payment_method_types_payments (type_id, method_id)
SELECT 1, 'd2125df0-2889-4e74-b264-8c4d0c04ab85'
WHERE NOT EXISTS (
    SELECT 1 FROM public.payment_method_types_payments
    WHERE type_id = 1 AND method_id = 'd2125df0-2889-4e74-b264-8c4d0c04ab85'
);

INSERT INTO public.payment_method_types_payments (type_id, method_id)
SELECT 2, 'd2125df0-2889-4e74-b264-8c4d0c04ab85'
WHERE NOT EXISTS (
    SELECT 1 FROM public.payment_method_types_payments
    WHERE type_id = 2 AND method_id = 'd2125df0-2889-4e74-b264-8c4d0c04ab85'
);

INSERT INTO public.payment_method_types_payments (type_id, method_id)
SELECT 2, 'fa36ab99-c173-4ded-a09f-a9910d630bcc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.payment_method_types_payments
    WHERE type_id = 2 AND method_id = 'fa36ab99-c173-4ded-a09f-a9910d630bcc'
);

INSERT INTO public.payment_method_types_payments (type_id, method_id)
SELECT 3, 'd2125df0-2889-4e74-b264-8c4d0c04ab85'
WHERE NOT EXISTS (
    SELECT 1 FROM public.payment_method_types_payments
    WHERE type_id = 3 AND method_id = 'd2125df0-2889-4e74-b264-8c4d0c04ab85'
);

INSERT INTO public.payment_method_types_payments (type_id, method_id)
SELECT 3, '9e86d6db-2e17-481d-b82f-6b033e76086f'
WHERE NOT EXISTS (
    SELECT 1 FROM public.payment_method_types_payments
    WHERE type_id = 3 AND method_id = '9e86d6db-2e17-481d-b82f-6b033e76086f'
);

-- insert topics
INSERT INTO public.topics (created_at, created_by, last_modified_at, last_modified_by, is_deleted, topic_name)
SELECT CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', false, 'ORDER'
WHERE NOT EXISTS (SELECT 1 FROM public.topics WHERE topic_name = 'ORDER');

INSERT INTO public.topics (created_at, created_by, last_modified_at, last_modified_by, is_deleted, topic_name)
SELECT CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', false, 'ACCOUNT'
WHERE NOT EXISTS (SELECT 1 FROM public.topics WHERE topic_name = 'ACCOUNT');

INSERT INTO public.topics (created_at, created_by, last_modified_at, last_modified_by, is_deleted, topic_name)
SELECT CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', false, 'RESERVATION'
WHERE NOT EXISTS (SELECT 1 FROM public.topics WHERE topic_name = 'RESERVATION');