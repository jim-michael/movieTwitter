import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from 'User';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  constructor(private http: HttpClient) {}

  checkLoginStatus(): Observable<User> {
    return this.http.get<User>('http://localhost:8080/loginstatus', {
      withCredentials: true,
    });
  }

  login(username: String, password: String): Observable<User> {
    return this.http.post<User>(
      'http://localhost:8080/login',
      {
        username: username,
        password: password,
      },
      { withCredentials: true }
    );
  }

  logout(): Observable<User> {
    return this.http.post<User>(
      'http://localhost:8080/logout',
      {},
      { withCredentials: true }
    );
  }

  signup(username: String, password: String): Observable<User> {
    return this.http.post<User>(
      'http://localhost:8080/signup',
      {
        username: username,
        password: password,
      },
      { withCredentials: true }
    );
  }
}
