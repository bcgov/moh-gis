resource "aws_sns_topic" "gis-sns" {
  name = "gis-topic"
}

resource "aws_sns_topic_subscription" "gis-email-target" {
  topic_arn = gis-sns.topic.arn
  protocol  = "email"
  endpoint  = aws_secretsmanager_secret_version.gis_sns_email.arn
}
