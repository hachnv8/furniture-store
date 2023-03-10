import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { RegisterRequest } from 'src/app/interfaces/register-request';
import { environment } from 'src/environments/environment';

import { User } from '../models/auth.models';
import { LocalStoreService } from './local-storage.service';

@Injectable({ providedIn: 'root' })

export class AuthenticationService {

    user: User;

    constructor(
        private httpClient: HttpClient,
        private localStoreService: LocalStoreService,
        private router: Router
    ) {
        
    }

    /**
     * Returns the current user
     */
    public currentUser() {
        return this.localStoreService.userData;
    }

    /**
     * Performs the auth
     * @param email email of user
     * @param password password of user
     */
    login(body: { email: string, password: string }) {
        return this.httpClient.post(`${environment.apiUrl}auth/signin`, body);
    }

    /**
     * Performs the register
     * @param email email
     * @param password password
     */
    register(body: RegisterRequest) {
        return this.httpClient.post(`${environment.apiUrl}auth/signup`, body);
    }

    /**
     * Reset password
     * @param email email
     */
    resetPassword(email: string) {
        
    }

    /**
     * Logout the user
     */
    logout() {
        // logout the user
       
    }
}

