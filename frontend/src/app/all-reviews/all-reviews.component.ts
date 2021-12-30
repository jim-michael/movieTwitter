import { Component, Input, OnInit } from '@angular/core';
import { Movie } from 'Movie';
import { Review } from 'Review';
import { User } from 'User';
import { AuthenticationService } from '../authentication.service';
import { ReviewService } from '../review.service';

@Component({
  selector: 'app-all-reviews',
  templateUrl: './all-reviews.component.html',
  styleUrls: ['./all-reviews.component.css'],
})
export class AllReviewsComponent implements OnInit {
  constructor(
    private authenticationService: AuthenticationService,
    private reviewService: ReviewService
  ) {}

  loggedInUser!: User;
  movies!: Movie[];
  reviews!: Review[];

  @Input()
  addedReview!: Review;

  public clickedEvent!: Event;
  public clickedFilterEvent!: Event;

  ngOnInit() {
    // this.authenticationService.checkLoginStatus().subscribe((user) => {
    //   this.loggedInUser = user;
    // });
    // this.reviewService.getAllReviews().subscribe((review) => {
    //   this.reviews = review;
    //   console.log(this.reviews);
    // });
  }

  childEventClicked(event: Event) {
    this.clickedEvent = event;
  }

  childFilterEventClicked(event: Event) {
    this.clickedFilterEvent = event;
  }
}
