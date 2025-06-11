
### public subnet ####
resource "aws_subnet" "sample-subnet-public01" {
    vpc_id = aws_vpc.sample-vpc.id
    cidr_block = "10.0.0.0/20"
    availability_zone = "us-east-2a"
    map_public_ip_on_launch = "true"
    tags = {
      "Name" = "sample-subnet-public01"
    } 
}

resource "aws_subnet" "sample-subnet-public02" {
    vpc_id = aws_vpc.sample-vpc.id
    cidr_block = "10.0.16.0/20"
    availability_zone = "us-east-2c"
    map_public_ip_on_launch = "true"
    tags = {
      "Name" = "sample-subnet-public02"
    } 
}

## pirvate subnet
resource "aws_subnet" "sample-subnet-private01" {
    vpc_id = aws_vpc.sample-vpc.id
    cidr_block = "10.0.64.0/20"
    availability_zone = "us-east-2a"
    tags = {
      "Name" = "sample-subnet-private01"
    } 
}

resource "aws_subnet" "sample-subnet-private02" {
    vpc_id = aws_vpc.sample-vpc.id
    cidr_block = "10.0.80.0/20"
    availability_zone = "us-east-2c"
    tags = {
      "Name" = "sample-subnet-private02"
    } 
}