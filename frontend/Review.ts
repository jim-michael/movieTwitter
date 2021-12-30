export interface Review {
  id: Number;
  title: String;
  reviewText: String;
  rating: Number;
  submissionTime: Date;
  movieApiId: String;
  author: {
    id: Number;
    username: String;
  };
}
