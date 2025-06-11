resource "aws_route_table_association" "rt_associate-public01" {
    subnet_id = aws_subnet.sample-subnet-public01.id
    route_table_id = aws_route_table.sample-rt-public.id 
}

resource "aws_route_table_association" "rt_associate-public02" {
    subnet_id = aws_subnet.sample-subnet-public02.id
    route_table_id = aws_route_table.sample-rt-public.id
}

resource "aws_route_table_association" "rt_associate-private01" {
    subnet_id = aws_subnet.sample-subnet-private01.id
    route_table_id = aws_route_table.sample-rt-private01.id
}

resource "aws_route_table_association" "rt_associate-private02" {
    subnet_id = aws_subnet.sample-subnet-private02.id
    route_table_id = aws_route_table.sample-rt-private02.id
}