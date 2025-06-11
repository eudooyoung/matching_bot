output "bastion_public_ip" {
  description = "Public IP address of the bastion instance"
  value       = aws_instance.sample-ec2-bastion.public_ip
}

output "jenkins_public_ip" {
  description = "Public IP address of the jenkins instance"
  value       = aws_instance.jenkins_server.public_ip
}

output "jenkins_private_ip" {
  description = "Private IP address of the jenkins instance"
  value       = aws_instance.jenkins_server.private_ip
}