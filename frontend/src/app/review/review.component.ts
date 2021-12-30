import { DatePipe } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { Review } from 'Review';
import { ReviewService } from '../review.service';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css'],
})
export class ReviewComponent implements OnInit {
  constructor(
    private reviewService: ReviewService,
    private datePipe: DatePipe
  ) {}

  reviews!: Review[];

  @Input()
  reviewToAdd!: Review;

  @Input()
  filterEvent!: Event;

  noReviewsFound: Boolean = false;
  showingAllReviews: Boolean = true;

  getAllReviews() {
    this.reviewService.getAllReviews().subscribe((reviews) => {
      this.reviews = reviews;
      this.showingAllReviews = true;
      this.noReviewsFound = false;
    });
  }

  ngOnInit() {
    this.reviewService.getAllReviews().subscribe((review) => {
      this.reviews = review;
    });
  }
  ngOnChanges() {
    if (this.reviewToAdd === undefined || this.reviewToAdd.id === 0) {
      if (<String>(<unknown>this.filterEvent))
        this.reviewService
          .getAllReviewsByMovieId(this.filterEvent)
          .subscribe((data) => {
            if (data.length === 0) {
              this.noReviewsFound = true;
              this.reviews = data;
              this.showingAllReviews = false;
            } else {
              this.noReviewsFound = false;
              this.showingAllReviews = false;
              this.reviews = data;
            }
          });
    } else {
      this.reviewService.getAllReviews().subscribe((reviews) => {
        this.reviews = reviews;
        this.noReviewsFound = false;
        this.showingAllReviews = true;
        this.reviewToAdd = {
          id: 0,
          title: '',
          reviewText: '',
          author: {
            id: 0,
            username: '',
          },
          submissionTime: new Date(),
          movieApiId: '',
          rating: 0,
        };
      });
    }
  }

  convertDate(date: Date) {
    return this.datePipe.transform(date, 'MMM d, y, h:mm:ss a');
  }
}
