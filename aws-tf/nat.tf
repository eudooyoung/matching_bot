resource "aws_eip" "nat-ip01" {
    domain = "vpc" 
  
}

resource "aws_eip" "nat-ip02" {
    domain = "vpc" 
  
}

resource "aws_nat_gateway" "sample-ngw-01" {
    allocation_id = aws_eip.nat-ip01.id
    subnet_id = aws_subnet.sample-subnet-public01.id
    tags = {
      "Name" = "sample-ngw-01"
    }
  
}

resource "aws_nat_gateway" "sample-ngw-02" {
    allocation_id = aws_eip.nat-ip02.id
    subnet_id = aws_subnet.sample-subnet-public02.id
    tags = {
      "Name" = "sample-ngw-02"
    }
  
}
