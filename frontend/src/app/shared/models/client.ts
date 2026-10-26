/**
 * Client models
 *
 * Shapes of the clients exchanged with the API.
 *
 * @author adrianpmdev
 */

export interface Client {
    id: number;
    name: string;
    email: string | null;
    phone: string | null;
    createdAt: string;
}

export interface ClientRequest {
    name: string;
    email: string | null;
    phone: string | null;
}
