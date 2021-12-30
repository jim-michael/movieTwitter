import { HttpErrorResponse } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { Review } from 'Review';
import { User } from 'User';
import { AuthenticationService } from './authentication.service';
import { ReviewService } from './review.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  constructor(
    private authenticationService: AuthenticationService,
    private reviewService: ReviewService
  ) {}

  title = 'movie-tweeter-frontend';

  loginModalIsActive: Boolean = false;
  signupModalIsActive: Boolean = false;
  reviewModalIsActive: Boolean = false;

  loggedInUser!: User;

  loginUsername!: String;
  loginPassword!: String;

  signupUsername!: String;
  signupPassword!: String;

  reviewTitle!: String;
  reviewText!: String;
  reviewRating!: String;
  reviewMovieId!: String;

  invalidSignupAttempt: Boolean = false;

  invalidLoginAttempt: Boolean = false;

  invalidReviewAttempt: Boolean = false;
  reviewErrorMessage: String = '';

  reviewToAdd!: Review;

  ngOnInit() {
    this.authenticationService.checkLoginStatus().subscribe((user) => {
      this.loggedInUser = user;
    });
  }

  activateModal() {
    this.loginModalIsActive = true;
  }

  activateSignupModal() {
    this.signupModalIsActive = true;
  }

  deactivateLoginModal() {
    this.loginModalIsActive = false;
  }

  deactivateSignupModal() {
    this.signupModalIsActive = false;
  }

  activateReviewModal() {
    this.reviewModalIsActive = true;
  }

  deactivateReviewModal() {
    this.reviewModalIsActive = false;
  }

  loginUser() {
    this.authenticationService
      .login(this.loginUsername, this.loginPassword)
      .subscribe(
        (user) => {
          this.loggedInUser = user;
          this.loginModalIsActive = false;
        },
        (error: HttpErrorResponse) => {
          this.invalidLoginAttempt = true;
        }
      );
  }

  logoutUser() {
    this.authenticationService.logout().subscribe((user) => {
      this.loggedInUser = user;
      location.reload();
    });
  }

  createReview() {
    this.reviewService
      .createReview(
        this.reviewTitle,
        this.reviewText,
        this.reviewRating,
        this.reviewMovieId
      )
      .subscribe({
        next: (res) => {
          if (res.status === 201) {
            this.deactivateReviewModal();
            this.reviewToAdd = <Review>res.body;
          }
        },
        error: (err) => {
          this.invalidReviewAttempt = true;
          this.reviewErrorMessage = err.error;
        },
      });
  }

  signupNewUser() {
    this.authenticationService
      .signup(this.signupUsername, this.signupPassword)
      .subscribe(
        (user) => {
          this.loggedInUser = user;
          this.signupModalIsActive = false;
          location.reload();
        },
        (error: HttpErrorResponse) => {
          this.invalidSignupAttempt = true;
        }
      );
  }
}
